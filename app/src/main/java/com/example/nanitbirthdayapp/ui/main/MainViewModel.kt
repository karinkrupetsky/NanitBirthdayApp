package com.example.nanitbirthdayapp.ui.main

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nanitbirthdayapp.data.model.BirthdayInfo
import com.example.nanitbirthdayapp.data.repository.BirthdayRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MainUiState(
    val isLoading: Boolean = false,
    val birthdayInfo: BirthdayInfo? = null,
    val error: String? = null,
    val imageUri: Uri? = null
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: BirthdayRepository
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

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch {
            repository.closeConnection()
        }
    }
}
