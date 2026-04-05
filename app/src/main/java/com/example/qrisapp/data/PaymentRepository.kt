package com.example.qrisapp.data

import com.example.qrisapp.model.Payment
import com.example.qrisapp.network.SupabaseClient
import io.github.jan.supabase.postgrest.from

class PaymentRepository {
    suspend fun getListPembayaran(): List<Payment> {
        return try {
            SupabaseClient.client
                .from("pembayaran")
                .select()
                .decodeList<Payment>()
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun simpanPembayaran(payment: Payment): Boolean {
        return try {
            SupabaseClient.client
                .from("pembayaran")
                .insert(payment)
            true
        } catch (e: Exception) {
            false
        }
    }
}