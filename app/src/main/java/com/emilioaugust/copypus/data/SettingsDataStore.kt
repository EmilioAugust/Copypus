package com.emilioaugust.copypus.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.emilioaugust.copypus.ui.theme.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(
    name = "settings"
)

class SettingsDataStore(private val context: Context) {
    companion object {
        private val THEME_MODE = stringPreferencesKey("theme_mode")
        private val AUTO_DELETE_KEY = stringPreferencesKey("auto_delete")
        private val APP_LANGUAGE_KEY = stringPreferencesKey("app_language")

        private val monitoringEnabledKey = booleanPreferencesKey("monitoring_enabled")
        private val pauseDurationKey = stringPreferencesKey("pause_duration")
        private val pausedUntilKey = longPreferencesKey("paused_until")
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

    val language: Flow<AppLanguage> = context.dataStore.data.map { speech ->
        AppLanguage.valueOf(
            speech[APP_LANGUAGE_KEY] ?: AppLanguage.ENGLISH.name
        )
    }

    val monitoringEnabledFlow: Flow<Boolean> = context.dataStore.data.map {
            it[monitoringEnabledKey] ?: true
        }

    val pauseDurationFlow: Flow<PauseDuration> = context.dataStore.data.map {
            PauseDuration.valueOf(
                it[pauseDurationKey] ?: PauseDuration.MIN_15.name
            )
        }

    val pausedUntilFlow: Flow<Long> = context.dataStore.data.map {
            it[pausedUntilKey] ?: 0L
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

    suspend fun setAppLanguage(language: AppLanguage) {
        context.dataStore.edit {
            it[APP_LANGUAGE_KEY] = language.name
        }
    }

    suspend fun getAppLanguage(): AppLanguage {
        val prefs = context.dataStore.data.first()

        val saved = prefs[APP_LANGUAGE_KEY] ?: AppLanguage.SYSTEM.name

        return AppLanguage.valueOf(saved)
    }

    suspend fun setMonitoringEnabled(enabled: Boolean) {
        context.dataStore.edit {
            it[monitoringEnabledKey] = enabled
        }
    }

    suspend fun setPauseDuration(duration: PauseDuration) {
        context.dataStore.edit {
            it[pauseDurationKey] = duration.name
        }
    }

    suspend fun pauseMonitoring(duration: PauseDuration) {
        val until = System.currentTimeMillis() + duration.minutes * 60 * 1000

        context.dataStore.edit {
            it[monitoringEnabledKey] = false
            it[pausedUntilKey] = until
        }
    }
}