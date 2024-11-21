package com.example.storyapp.data.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class SessionPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    fun getSessionToken(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[SESSION_TOKEN]
        }
    }

    suspend fun saveSessionToken(token: String) {
        dataStore.edit { preferences ->
            preferences[SESSION_TOKEN] = token
        }
    }

    suspend fun clearSessionToken() {
        dataStore.edit { preferences ->
            preferences.remove(SESSION_TOKEN)
        }
    }

    fun getUserId(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[USER_ID]
        }
    }

    suspend fun saveUserId(userId: String) {
        dataStore.edit { preferences ->
            preferences[USER_ID] = userId
        }
    }

    suspend fun clearUserId() {
        dataStore.edit { preferences ->
            preferences.remove(USER_ID)
        }
    }

    fun getName(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[NAME]
        }
    }

    suspend fun saveName(name: String) {
        dataStore.edit { preferences ->
            preferences[NAME] = name
        }
    }

    suspend fun clearName() {
        dataStore.edit { preferences ->
            preferences.remove(NAME)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: SessionPreferences? = null

        private val SESSION_TOKEN = stringPreferencesKey("session_token")
        private val USER_ID = stringPreferencesKey("user_id")
        private val NAME = stringPreferencesKey("name")

        fun getInstance(dataStore: DataStore<Preferences>): SessionPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = SessionPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}