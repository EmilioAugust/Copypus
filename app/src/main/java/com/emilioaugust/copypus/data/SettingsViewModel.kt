package com.emilioaugust.copypus.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.emilioaugust.copypus.ui.theme.ThemeMode
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(private val dataStore: SettingsDataStore) : ViewModel() {
    val themeMode = dataStore.themeMode
    val autoDelete = dataStore.autoDelete.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AutoDeleteOption.NEVER
    )
    val language =
        dataStore.language
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                AppLanguage.SYSTEM
            )
    val pauseDuration = dataStore.pauseDurationFlow

    fun setThemeMode(mode: ThemeMode) {
        viewModelScope.launch {
            dataStore.setThemeMode(mode)
        }
    }

    fun setAutoDelete(option: AutoDeleteOption) {
        viewModelScope.launch {
            dataStore.setAutoDelete(option)
        }
    }

    suspend fun setLanguage(language: AppLanguage) {
        dataStore.setAppLanguage(language)
    }

    fun setPauseDuration(duration: PauseDuration) {
        viewModelScope.launch {
            dataStore.setPauseDuration(duration)
        }
    }


}

class SettingsViewModelFactory(private val dataStore: SettingsDataStore) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(dataStore) as T
    }
}