package com.example.nanitbirthdayapp.ui.birthday

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nanitbirthdayapp.R
import com.example.nanitbirthdayapp.core.Resource
import com.example.nanitbirthdayapp.domain.model.Birthday
import com.example.nanitbirthdayapp.domain.usecase.CloseConnectionUseCase
import com.example.nanitbirthdayapp.domain.usecase.GetBirthdayInfoUseCase
import com.example.nanitbirthdayapp.domain.usecase.GetSavedPictureUseCase
import com.example.nanitbirthdayapp.domain.exception.InvalidConnectionParametersException
import com.example.nanitbirthdayapp.domain.usecase.UpdateBabyPictureUseCase
import com.example.nanitbirthdayapp.util.ShareHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
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
    val errorMessage: String? = null
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
            _uiState.value = BirthdayUiState(isLoading = true)
            val portNumber = port.toIntOrNull()
            if (portNumber == null) {
                _uiState.value = BirthdayUiState(errorMessage = context.getString(R.string.invalid_port_number))
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
                    val errorMessage = when (throwable) {
                        is InvalidConnectionParametersException -> context.getString(R.string.invalid_ip_or_port)
                        else -> throwable.message
                    }
                    _uiState.value = BirthdayUiState(errorMessage = errorMessage)
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

        _uiState.update { it.copy(selectedImageUri = pictureUri) }

        viewModelScope.launch {
            updateBabyPictureUseCase(
                name = currentBirthdayInfo.name,
                dob = currentBirthdayInfo.dob,
                pictureUri = pictureUri
            ).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is Resource.Success -> _uiState.update {
                        it.copy(
                            isLoading = false, 
                            selectedImageUri = resource.data, 
                            errorMessage = null
                        )
                    }
                    is Resource.Error -> _uiState.update {
                        it.copy(
                            isLoading = false, 
                            errorMessage = resource.message
                        )
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
                val bitmap = onCaptureContent()
                if (bitmap != null) {
                    val success = ShareHelper.saveBitmapAndShare(context, bitmap)
                    if (!success) {
                        _uiState.update { it.copy(errorMessage = context.getString(R.string.failed_to_share_image)) }
                    }
                } else {
                    _uiState.update { it.copy(errorMessage = context.getString(R.string.failed_to_capture_screen)) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = context.getString(R.string.share_failed, e.message ?: "")) }
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