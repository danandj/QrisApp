package com.example.qrisapp.model

import kotlinx.serialization.Serializable

@Serializable
data class QrPayment(
    val reference_id: String,
    val currency: String,
    val amount: Int
)