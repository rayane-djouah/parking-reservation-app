package com.example.m_parking.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.Provides
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject


val Context.firebasetokendataStore: DataStore<Preferences> by preferencesDataStore(name = "firebase_token_pref")

class FirebaseTokenStoreRepository(context: Context) {

    private object PreferencesKey {
        val tokenKey = stringPreferencesKey(name = "firebase_token_pref")
    }

    private var _cachedToken: String? = null


    companion object {
        val Context.firebasetokendataStore: DataStore<Preferences> by preferencesDataStore(name = "firebase_token_pref")
    }

    private val firebasetokendataStore = context.firebasetokendataStore

    init {
        // Initialize and listen to token changes
        GlobalScope.launch {
            readTokenState().collect { newToken ->
                _cachedToken = newToken
            }
        }
    }

    suspend fun saveTokenState(token: String) {
        firebasetokendataStore.edit { preferences ->
            preferences[PreferencesKey.tokenKey] = token
        }
    }

    fun readTokenState(): Flow<String> {
        return firebasetokendataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[PreferencesKey.tokenKey] ?: ""
            }
    }

    fun readCachedTokenState(): String {
        return _cachedToken ?: ""
    }

}