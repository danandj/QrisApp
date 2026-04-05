package com.example.qrisapp.data

import com.example.qrisapp.model.UserSaldo
import com.example.qrisapp.network.SupabaseClient
import io.github.jan.supabase.postgrest.from

class BalanceRepository {

    suspend fun getSaldo(userId: Long): Double? {
        return try {
            val result = SupabaseClient.client
                .from("user")
                .select {
                    filter { eq("user_id", userId) }
                }
                .decodeSingle<UserSaldo>()

            result.saldo
        } catch (e: Exception) {
            null
        }
    }

    suspend fun updateSaldo(userId: Long, saldo: Double): Boolean {
        return try {
            SupabaseClient.client
                .from("user")
                .update(mapOf("saldo" to saldo)) {
                    filter { eq("user_id", userId) }
                }
            true
        } catch (_: Exception) {
            false
        }
    }
}