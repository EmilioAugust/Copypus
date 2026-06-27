package com.emilioaugust.copypus.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.emilioaugust.copypus.ui.theme.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(
    name = "settings"
)

class SettingsDataStore(private val context: Context) {
    companion object {
        private val THEME_MODE = stringPreferencesKey("theme_mode")
        private val AUTO_DELETE_KEY = stringPreferencesKey("auto_delete")
    }

    val themeMode: Flow<ThemeMode> = context.dataStore.data.map {
        ThemeMode.valueOf(
            it[THEME_MODE]
                ?: ThemeMode.SYSTEM.name
        )
    }

    val autoDelete = context.dataStore.data.map { preferences ->
        AutoDeleteOption.valueOf(
            preferences[AUTO_DELETE_KEY] ?: AutoDeleteOption.NEVER.name
        )
    }

    suspend fun setAutoDelete(option: AutoDeleteOption) {
        context.dataStore.edit { preferences ->
            preferences[AUTO_DELETE_KEY] = option.name
        }
    }

    suspend fun setThemeMode(mode: ThemeMode) {
        context.dataStore.edit {

            it[THEME_MODE] = mode.name
        }
    }
}