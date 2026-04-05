package com.example.qrisapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.qrisapp.data.BalanceRepository
import com.example.qrisapp.data.PaymentRepository
import com.example.qrisapp.data.SessionManager
import com.example.qrisapp.model.PaymentInsert
import com.example.qrisapp.model.QrPayment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ScanUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)

class ScanQrViewModel(
    private val sessionManager: SessionManager,
    private val balanceRepository: BalanceRepository,
    private val paymentRepository: PaymentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScanUiState())
    val uiState: StateFlow<ScanUiState> = _uiState.asStateFlow()

    private val json = Json { ignoreUnknownKeys = true }

    fun processQrResult(qrData: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, isSuccess = false) }
            try {
                // 1. Validate JSON format
                val qrPayment = try {
                    json.decodeFromString<QrPayment>(qrData)
                } catch (_: Exception) {
                    throw Exception("Format QR tidak valid")
                }

                // 2. Get User Session
                val user = sessionManager.getUser() ?: throw Exception("Sesi pengguna tidak ditemukan")

                // 3. Validate Balance
                val currentBalance = balanceRepository.getSaldo(user.user_id) ?: 0.0
                if (currentBalance < qrPayment.amount) {
                    throw Exception("Saldo tidak mencukupi")
                }

                // 4. Update Balance
                val newBalance = currentBalance - qrPayment.amount
                val updateSuccess = balanceRepository.updateSaldo(user.user_id, newBalance)
                if (!updateSuccess) {
                    throw Exception("Gagal memperbarui saldo")
                }

                // Update local session to ensure Dashboard reflects changes immediately
                sessionManager.saveUser(
                    userId = user.user_id,
                    username = user.username,
                    nama = user.nama_lengkap
                )

                // 5. Save Transaction History
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val paymentInsert = PaymentInsert(
                    referensi_id = qrPayment.reference_id,
                    tanggal_bayar = dateFormat.format(Date()),
                    jumlah_bayar = qrPayment.amount.toDouble(),
                    currency = qrPayment.currency,
                    user_id = user.user_id
                )

                val saveSuccess = paymentRepository.simpanPembayaran(paymentInsert)
                if (!saveSuccess) {
                    throw Exception("Gagal menyimpan riwayat transaksi")
                }

                _uiState.update { it.copy(isLoading = false, isSuccess = true) }

            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message ?: "Terjadi kesalahan") }
            }
        }
    }

    fun resetError() {
        _uiState.update { it.copy(errorMessage = null, isSuccess = false, isLoading = false) }
    }

    class Factory(
        private val sessionManager: SessionManager,
        private val balanceRepository: BalanceRepository,
        private val paymentRepository: PaymentRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ScanQrViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ScanQrViewModel(sessionManager, balanceRepository, paymentRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
