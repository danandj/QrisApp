package com.example.qrisapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qrisapp.data.AuthRepository
import com.example.qrisapp.data.SessionManager
import kotlinx.coroutines.launch

class PinViewModel(
    private val authRepository: AuthRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    var pinInput by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var isSuccess by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    fun onPinChange(newPin: String) {
        if (newPin.length <= 6) {
            pinInput = newPin
            if (pinInput.length == 6) {
                verifyPin()
            }
        }
    }

    private fun verifyPin() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            
            val user = sessionManager.getUser()
            if (user != null) {
                val isValid = authRepository.cekpin(user.username, pinInput)
                if (isValid) {
                    isSuccess = true
                } else {
                    errorMessage = "PIN yang anda masukan salah"
                    pinInput = "" // Reset PIN if wrong
                }
            } else {
                errorMessage = "Sesi tidak ditemukan, silahkan login kembali"
            }
            isLoading = false
        }
    }

    fun resetError() {
        errorMessage = null
    }

    class Factory(
        private val authRepository: AuthRepository,
        private val sessionManager: SessionManager
    ) : androidx.lifecycle.ViewModelProvider.Factory {
        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PinViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return PinViewModel(authRepository, sessionManager) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}