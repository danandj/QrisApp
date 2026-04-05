package com.example.qrisapp.data

import android.util.Log
import com.example.qrisapp.model.User
import com.example.qrisapp.network.SupabaseClient
import io.github.jan.supabase.postgrest.from

class AuthRepository {

    suspend fun login(username: String, password: String): Result<User> {
        return try {
            val result = SupabaseClient.client
                .from("user")
                .select {
                    filter {
                        eq("username", username)
                        eq("password", password)
                    }
                }
                .decodeList<User>()
            if (result.isNotEmpty()) {
                Result.success(result.first())
            } else {
                Result.failure(Exception("Username atau Password salah"))
            }

        } catch (e: Exception) {
            e.message?.let { Log.d("TAG", it) }
            Result.failure(e)
        }
    }

    suspend fun cekpin(username: String, pin: String): Boolean {
        return try {
            val result = SupabaseClient.client
                .from("user")
                .select {
                    limit(1)
                    filter {
                        eq("username", username)
                        eq("pin", pin)
                    }
                }.decodeList<User>()
            result.isNotEmpty()
        } catch (e: Exception) {
            e.message?.let { Log.d("TAG", it) }
            false
        }
    }
}