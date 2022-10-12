package com.binar.latihan_datastore_login.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LoginDataStoreManager(private val context: Context) {

    suspend fun setUser(name: String, password: String) {
        context.loginDataStore.edit { preferences ->
            preferences[USERNAME_KEY] = name
            preferences[PASSWORD_KEY] = password
        }
    }

    suspend fun setUserLogin(isLogin: Boolean) {
        context.loginDataStore.edit { preferences ->
            preferences[LOGIN_STATUS_KEY] = isLogin
        }
    }

    fun getUser(): Flow<UserPreferences> {
        return context.loginDataStore.data.map { preferences ->
            UserPreferences(
                preferences[USERNAME_KEY] ?: "",
                preferences[PASSWORD_KEY] ?: "",
                preferences[LOGIN_STATUS_KEY] ?: false)
        }
    }

    fun getUserLogin(): Flow<Boolean> {
        return context.loginDataStore.data.map { preferences ->
            preferences[LOGIN_STATUS_KEY] ?: false
        }
    }

    companion object {
        private const val DATA_STORE_NAME = "login_preferences"
        private val USERNAME_KEY = stringPreferencesKey("name_key")
        private val PASSWORD_KEY = stringPreferencesKey("password_key")
        private val LOGIN_STATUS_KEY = booleanPreferencesKey("login_status_key")

        val Context.loginDataStore: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_NAME)
    }
}