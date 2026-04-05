package com.example.qrisapp.data

import com.example.qrisapp.model.Payment
import com.example.qrisapp.model.PaymentInsert
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

    suspend fun simpanPembayaran(paymentInsert: PaymentInsert): Boolean {
        return try {
            SupabaseClient.client
                .from("pembayaran")
                .insert(paymentInsert)
            true
        } catch (e: Exception) {
            false
        }
    }
}