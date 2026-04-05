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
    }

    suspend fun saveUser(userId: Long, username: String, nama: String) {
        context.dataStore.edit {
            it[USER_ID] = userId
            it[USERNAME] = username
            it[NAMA] = nama
        }
    }

    suspend fun getUser(): User? {
        val preferences = context.dataStore.data.first()

        val userId = preferences[USER_ID] ?: return null
        val username = preferences[USERNAME] ?: return null
        val nama = preferences[NAMA] ?: return null

        return User(
            user_id = userId,
            username = username,
            nama_lengkap = nama
        )
    }

    suspend fun clearSession() {
        context.dataStore.edit { it.clear() }
    }
}