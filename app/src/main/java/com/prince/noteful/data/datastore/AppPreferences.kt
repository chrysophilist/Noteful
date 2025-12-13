package com.prince.noteful.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore by preferencesDataStore(
    name = "app_preferences"
)

object PreferencesKeys {

    val USER_NAME = stringPreferencesKey("user_name")
    val GRID_VIEW = booleanPreferencesKey("grid_view")
}