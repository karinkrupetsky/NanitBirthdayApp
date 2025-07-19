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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BirthdayViewModel @Inject constructor(
    private val getBirthdayInfoUseCase: GetBirthdayInfoUseCase,
    private val updateBabyPictureUseCase: UpdateBabyPictureUseCase,
    private val birthdayRepository: BirthdayRepository
) : ViewModel() {

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _birthdayInfo = MutableStateFlow<Birthday?>(null)
    val birthdayInfo: StateFlow<Birthday?> = _birthdayInfo.asStateFlow()

    private val _selectedImageUri = MutableStateFlow<Uri?>(null)
    val selectedImageUri: StateFlow<Uri?> = _selectedImageUri.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    /**
     * Connect to the server and retrieve birthday info.
     */
    fun connectToServer(ip: String, port: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            val portNumber = port.toIntOrNull()
            if (portNumber == null) {
                _isLoading.value = false
                _errorMessage.value = "Invalid port number."
                return@launch
            }

            getBirthdayInfoUseCase(ip, portNumber).collect { result ->
                result.onSuccess { birthdayInfo ->
                    _birthdayInfo.value = birthdayInfo
                    _isLoading.value = false
                    _errorMessage.value = null
                    // Load this baby's saved photo after updating state!
                    loadSavedPhoto(birthdayInfo.name)
                }.onFailure { throwable ->
                    _isLoading.value = false
                    _errorMessage.value = throwable.message
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
            _selectedImageUri.value = savedUri
        }
    }

    /**
     * Updates the baby picture with a new URI.
     */
    fun updatePicture(pictureUri: Uri?) {
        clearError()
        val babyKey = _birthdayInfo.value?.name ?: return

        if (pictureUri == null) {
            _selectedImageUri.value = null
            return
        }

        viewModelScope.launch {
            updateBabyPictureUseCase(babyKey, pictureUri).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _isLoading.value = true
                    }
                    is Resource.Success -> {
                        _isLoading.value = false
                        _selectedImageUri.value = resource.data
                        _errorMessage.value = null
                    }
                    is Resource.Error -> {
                        _isLoading.value = false
                        _errorMessage.value = resource.message
                    }
                }
            }
        }
    }

    /**
     * Clears the current error message.
     */
    fun clearError() {
        _errorMessage.value = null
    }

    /**
     * Sets an error message.
     */
    fun setError(message: String) {
        _errorMessage.value = message
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch {
            birthdayRepository.closeConnection()
        }
    }
}