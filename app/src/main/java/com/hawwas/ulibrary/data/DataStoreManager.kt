package com.hawwas.ulibrary.data

import android.content.*
import androidx.datastore.core.*
import androidx.datastore.preferences.*
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.*


class DataStoreManager(context: Context) {
    private val dataStore = context.userPreferencesDataStore

    companion object {
        private val Context.userPreferencesDataStore: DataStore<Preferences> by preferencesDataStore(
            name = "user"
        )

        val lastFetched = longPreferencesKey("last_fetched")
    }

    suspend fun saveLastFetchedTime(time: Long) {
        dataStore.edit { preferences ->
            preferences[lastFetched] = time
        }
    }

    suspend fun getLastFetchedTime(): Long {
        return dataStore.data.map { preferences ->
            preferences[lastFetched] ?: 0
        }.first()
    }
   suspend fun <T>  Preferences.Key<T>.save(value: T) {
        dataStore.edit { preferences ->
            preferences[this] = value
        }
    }
    suspend fun <T> Preferences.Key<T>.load(default:T): T {
        return dataStore.data.map { preferences ->
            preferences[this] ?: default
        }.first()
    }
}