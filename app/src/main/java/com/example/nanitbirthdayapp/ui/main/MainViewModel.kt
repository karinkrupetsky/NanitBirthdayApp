package com.example.nanitbirthdayapp.ui.main

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nanitbirthdayapp.data.model.BirthdayInfo
import com.example.nanitbirthdayapp.data.repository.BirthdayRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

data class MainUiState(
    val isLoading: Boolean = false,
    val birthdayInfo: BirthdayInfo? = null,
    val error: String? = null,
    val imageUri: Uri? = null,
    val selectedImageUri: Uri? = null // Added for photo selection
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: BirthdayRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    fun connectToServer(ip: String, port: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val portNumber = port.toIntOrNull()
            if (portNumber == null) {
                _uiState.update { it.copy(isLoading = false, error = "Invalid port number.") }
                return@launch
            }

            repository.getBirthdayInfo(ip, portNumber).collect { result ->
                result.onSuccess { birthdayInfo ->
                    _uiState.update {
                        it.copy(isLoading = false, birthdayInfo = birthdayInfo, error = null)
                    }
                }.onFailure { throwable ->
                    _uiState.update {
                        it.copy(isLoading = false, error = throwable.message)
                    }
                }
            }
        }
    }

    /**
     * Updates the baby picture with a new URI
     * Copies the image to internal storage for permanent access
     */
    fun updatePicture(pictureUri: Uri?) {
        clearError()

        if (pictureUri == null) {
            _uiState.update { it.copy(selectedImageUri = null) }
            return
        }

        viewModelScope.launch {
            try {
                val permanentUri = copyImageToInternalStorage(pictureUri)
                if (permanentUri != null) {
                    _uiState.update { it.copy(selectedImageUri = permanentUri) }
                } else {
                    _uiState.update { it.copy(error = "Failed to save image") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Error saving image: ${e.message}") }
            }
        }
    }

    /**
     * Copies an image from external URI to internal storage for permanent access
     */
    private suspend fun copyImageToInternalStorage(sourceUri: Uri): Uri? =
        withContext(Dispatchers.IO) {
            try {
                val inputStream = context.contentResolver.openInputStream(sourceUri)
                    ?: return@withContext null

                val internalDir = File(context.filesDir, "baby_images")
                if (!internalDir.exists()) {
                    internalDir.mkdirs()
                }

                val fileName = "baby_image_${System.currentTimeMillis()}.jpg"
                val destinationFile = File(internalDir, fileName)

                FileOutputStream(destinationFile).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }

                inputStream.close()
                Uri.fromFile(destinationFile)
            } catch (e: IOException) {
                null
            }
        }

    /**
     * Clears the current error message
     */
    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    /**
     * Sets an error message
     */
    fun setError(message: String) {
        _uiState.update { it.copy(error = message) }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch {
            repository.closeConnection()
        }
    }
}
