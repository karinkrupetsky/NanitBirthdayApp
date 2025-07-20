package com.example.nanitbirthdayapp.ui.birthday

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nanitbirthdayapp.core.Resource
import com.example.nanitbirthdayapp.domain.model.Birthday
import com.example.nanitbirthdayapp.domain.usecase.CloseConnectionUseCase
import com.example.nanitbirthdayapp.domain.usecase.GetBirthdayInfoUseCase
import com.example.nanitbirthdayapp.domain.usecase.GetSavedPictureUseCase
import com.example.nanitbirthdayapp.domain.usecase.UpdateBabyPictureUseCase
import com.example.nanitbirthdayapp.util.ShareHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BirthdayUiState(
    val isLoading: Boolean = false,
    val birthdayInfo: Birthday? = null,
    val selectedImageUri: Uri? = null,
    val errorMessage: String? = null,
    val isCapturingForShare: Boolean = false
)

@HiltViewModel
class BirthdayViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getBirthdayInfoUseCase: GetBirthdayInfoUseCase,
    private val updateBabyPictureUseCase: UpdateBabyPictureUseCase,
    private val getSavedPictureUseCase: GetSavedPictureUseCase,
    private val closeConnectionUseCase: CloseConnectionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(BirthdayUiState())
    val uiState: StateFlow<BirthdayUiState> = _uiState.asStateFlow()

    /**
     * Connect to the server and retrieve birthday info.
     */
    fun connectToServer(ip: String, port: String) {
        viewModelScope.launch {
            _uiState.value = BirthdayUiState(isLoading = true) // Reset state
            val portNumber = port.toIntOrNull()
            if (portNumber == null) {
                _uiState.value = BirthdayUiState(errorMessage = "Invalid port number.")
                return@launch
            }

            getBirthdayInfoUseCase(ip, portNumber).collect { result ->
                result.onSuccess { birthdayInfo ->
                    _uiState.update {
                        it.copy(isLoading = false, birthdayInfo = birthdayInfo, errorMessage = null)
                    }
                    // Load this baby's saved photo after updating state!
                    loadSavedPhoto(birthdayInfo.name, birthdayInfo.dob)
                }.onFailure { throwable ->
                    _uiState.value = BirthdayUiState(errorMessage = throwable.message)
                }
            }
        }
    }

    /**
     * Loads the saved photo for the given baby.
     */
    private fun loadSavedPhoto(name: String, dob: Long) {
        viewModelScope.launch {
            val savedUri = getSavedPictureUseCase(name, dob)
            _uiState.update { it.copy(selectedImageUri = savedUri) }
        }
    }

    /**
     * Updates the baby picture with a new URI.
     */
    fun updatePicture(pictureUri: Uri?) {
        clearError()
        val currentBirthdayInfo = _uiState.value.birthdayInfo ?: return

        if (pictureUri == null) {
            _uiState.update { it.copy(selectedImageUri = null) }
            return
        }

        viewModelScope.launch {
            updateBabyPictureUseCase(
                name = currentBirthdayInfo.name,
                dob = currentBirthdayInfo.dob,
                pictureUri = pictureUri
            ).collect { resource ->
                when (resource) {
                    is Resource.Loading -> _uiState.update { it.copy(isLoading = true) }
                    is Resource.Success -> _uiState.update {
                        it.copy(isLoading = false, selectedImageUri = resource.data, errorMessage = null)
                    }
                    is Resource.Error -> _uiState.update {
                        it.copy(isLoading = false, errorMessage = resource.message)
                    }
                }
            }
        }
    }

    /**
     * Clears the current error message.
     */
    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    /**
     * Sets an error message.
     */
    fun setError(message: String) {
        _uiState.update { it.copy(errorMessage = message) }
    }

    /**
     * Share birthday information.
     */
    fun shareBirthday(onCaptureContent: suspend () -> Bitmap?) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isCapturingForShare = true) }
                delay(100)
                //  Capture the content
                val bitmap = onCaptureContent()
                if (bitmap != null) {
                    //  Save bitmap and create share intent
                    val success = ShareHelper.saveBitmapAndShare(context, bitmap)
                    if (!success) {
                        _uiState.update { it.copy(errorMessage = "Failed to share image") }
                    }
                } else {
                    _uiState.update { it.copy(errorMessage = "Failed to capture screen") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Share failed: ${e.message}") }
            } finally {
                _uiState.update { it.copy(isCapturingForShare = false) }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch {
            closeConnectionUseCase()
        }
    }
}