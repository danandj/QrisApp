package com.example.qrisapp.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val user_id: Long,
    val username: String,
    val nama_lengkap: String,
    val saldo: Double
)