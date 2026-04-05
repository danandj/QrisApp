package com.example.qrisapp.model

import kotlinx.serialization.Serializable

@Serializable
data class Payment(
    val transaksi_id: Long,
    val referensi_id: String,
    val tanggal_bayar: String,
    val jumlah_bayar: Double? = null,
    val currency: String
)
