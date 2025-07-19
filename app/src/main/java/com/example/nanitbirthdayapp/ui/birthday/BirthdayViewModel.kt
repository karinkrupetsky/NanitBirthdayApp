package com.example.nanitbirthdayapp.ui.birthday

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nanitbirthdayapp.core.Resource
import com.example.nanitbirthdayapp.domain.model.Birthday
import com.example.nanitbirthdayapp.domain.repository.BirthdayRepository
import com.example.nanitbirthdayapp.domain.usecase.GetBirthdayInfoUseCase
import com.example.nanitbirthdayapp.domain.usecase.UpdateBabyPictureUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BirthdayUiState(
    val isLoading: Boolean = false,
    val birthdayInfo: Birthday? = null,
    val error: String? = null,
    val selectedImageUri: Uri? = null
)

@HiltViewModel
class BirthdayViewModel @Inject constructor(
    private val getBirthdayInfoUseCase: GetBirthdayInfoUseCase,
    private val updateBabyPictureUseCase: UpdateBabyPictureUseCase,
    private val birthdayRepository: BirthdayRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BirthdayUiState())
    val uiState: StateFlow<BirthdayUiState> = _uiState.asStateFlow()

    /**
     * Connect to the server and retrieve birthday info.
     */
    fun connectToServer(ip: String, port: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val portNumber = port.toIntOrNull()
            if (portNumber == null) {
                _uiState.update { it.copy(isLoading = false, error = "Invalid port number.") }
                return@launch
            }

            getBirthdayInfoUseCase(ip, portNumber).collect { result ->
                result.onSuccess { birthdayInfo ->
                    _uiState.update {
                        it.copy(isLoading = false, birthdayInfo = birthdayInfo, error = null)
                    }
                    // Load this baby's saved photo after updating state!
                    loadSavedPhoto(birthdayInfo.name)
                }.onFailure { throwable ->
                    _uiState.update {
                        it.copy(isLoading = false, error = throwable.message)
                    }
                }
            }
        }
    }

    /**
     * Loads the saved photo for the given babyKey (e.g., birthday name).
     */
    private fun loadSavedPhoto(babyKey: String) {
        viewModelScope.launch {
            val savedUri = birthdayRepository.getSavedPicture(babyKey)
            _uiState.update { it.copy(selectedImageUri = savedUri) }
        }
    }

    /**
     * Updates the baby picture with a new URI.
     */
    fun updatePicture(pictureUri: Uri?) {
        clearError()
        val babyKey = _uiState.value.birthdayInfo?.name ?: return

        if (pictureUri == null) {
            _uiState.update { it.copy(selectedImageUri = null) }
            return
        }

        viewModelScope.launch {
            updateBabyPictureUseCase(babyKey, pictureUri).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                selectedImageUri = resource.data,
                                error = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = resource.message
                            )
                        }
                    }
                }
            }
        }
    }

    /**
     * Clears the current error message.
     */
    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    /**
     * Sets an error message.
     */
    fun setError(message: String) {
        _uiState.update { it.copy(error = message) }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch {
            birthdayRepository.closeConnection()
        }
    }
}