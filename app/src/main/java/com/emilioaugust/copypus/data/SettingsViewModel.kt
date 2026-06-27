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

    fun setThemeMode(mode: ThemeMode) {
        viewModelScope.launch {
            dataStore.setThemeMode(mode)
        }
    }

    val autoDelete = dataStore.autoDelete.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AutoDeleteOption.NEVER
    )

    fun setAutoDelete(option: AutoDeleteOption) {
        viewModelScope.launch {
            dataStore.setAutoDelete(option)
        }
    }
}

class SettingsViewModelFactory(private val dataStore: SettingsDataStore) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(dataStore) as T
    }
}