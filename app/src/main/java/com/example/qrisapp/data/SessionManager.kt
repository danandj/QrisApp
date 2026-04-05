package com.example.qrisapp.data

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.qrisapp.model.User
import kotlinx.coroutines.flow.first

private val Context.dataStore by preferencesDataStore("session")

class SessionManager(private val context: Context) {

    companion object {
        val USER_ID = longPreferencesKey("user_id")
        val USERNAME = stringPreferencesKey("username")
        val NAMA = stringPreferencesKey("nama_lengkap")
        val SALDO = doublePreferencesKey("saldo")
    }

    suspend fun saveUser(userId: Long, username: String, nama: String, saldo: Double) {
        context.dataStore.edit {
            it[USER_ID] = userId
            it[USERNAME] = username
            it[NAMA] = nama
            it[SALDO] = saldo
        }
    }

    suspend fun getUser(): User? {
        val preferences = context.dataStore.data.first()

        val userId = preferences[USER_ID] ?: return null
        val username = preferences[USERNAME] ?: return null
        val nama = preferences[NAMA] ?: return null
        val saldo = preferences[SALDO] ?: return null

        return User(
            user_id = userId,
            username = username,
            nama_lengkap = nama,
            saldo = saldo
        )
    }

    suspend fun getUserId(): Long? {
        return context.dataStore.data.first()[USER_ID]
    }

    suspend fun getSaldo(): Double? {
        return context.dataStore.data.first()[SALDO]
    }

    suspend fun clearSession() {
        context.dataStore.edit { it.clear() }
    }
}