package com.prince.noteful.repo

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.prince.noteful.data.datastore.PreferencesKeys
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferencesRepository (
    private val dataStore: DataStore<Preferences>
) {

    // READ
    val userName: Flow<String?> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.USER_NAME]
    }

    val isGridView: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.GRID_VIEW] ?: true
    }

    // WRITES
    suspend fun saveUserName(name: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_NAME] = name
        }
    }

    suspend fun setGridView(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.GRID_VIEW] = enabled
        }
    }
}