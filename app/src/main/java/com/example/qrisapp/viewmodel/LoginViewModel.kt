package com.example.qrisapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qrisapp.data.AuthRepository
import com.example.qrisapp.data.SessionManager
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    var username by mutableStateOf("")
    var password by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var isSuccess by mutableStateOf(false)

    fun login() {
        if (username.isEmpty() || password.isEmpty()) {
            errorMessage = "Username dan password tidak boleh kosong"
            return
        }

        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            
            val result = authRepository.login(username, password)
            
            result.onSuccess { user ->
                sessionManager.saveUser(
                    userId = user.user_id,
                    username = user.username,
                    nama = user.nama_lengkap
                )
                isSuccess = true
            }.onFailure { exception ->
                errorMessage = exception.message ?: "Login Gagal"
            }
            
            isLoading = false
        }
    }

    class Factory(
        private val authRepository: AuthRepository,
        private val sessionManager: SessionManager
    ) : androidx.lifecycle.ViewModelProvider.Factory {
        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return LoginViewModel(authRepository, sessionManager) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}