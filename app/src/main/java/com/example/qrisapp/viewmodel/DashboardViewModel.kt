package com.example.qrisapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.qrisapp.data.BalanceRepository
import com.example.qrisapp.data.PaymentRepository
import com.example.qrisapp.data.SessionManager
import com.example.qrisapp.model.Payment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DashboardUiState(
    val userName: String = "",
    val balance: Double = 0.0,
    val transactions: List<Payment> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class DashboardViewModel(
    private val sessionManager: SessionManager,
    private val balanceRepository: BalanceRepository,
    private val paymentRepository: PaymentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        loadDashboardData()
    }

    fun loadDashboardData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val user = sessionManager.getUser()
                if (user != null) {
                    val balance = balanceRepository.getSaldo(user.user_id) ?: user.saldo
                    val transactions = paymentRepository.getListPembayaran()

                    _uiState.update {
                        it.copy(
                            userName = user.nama_lengkap,
                            balance = balance,
                            transactions = transactions,
                            isLoading = false
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Belum ada data pengguna"
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Gagal memuat data"
                    )
                }
            }
        }
    }

    class Factory(
        private val sessionManager: SessionManager,
        private val balanceRepository: BalanceRepository,
        private val paymentRepository: PaymentRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DashboardViewModel(sessionManager, balanceRepository, paymentRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
