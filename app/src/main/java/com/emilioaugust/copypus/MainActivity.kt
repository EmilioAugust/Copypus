package com.emilioaugust.copypus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.lifecycleScope
import com.emilioaugust.copypus.data.MainViewModel
import com.emilioaugust.copypus.data.SettingsDataStore
import com.emilioaugust.copypus.data.SettingsViewModel
import com.emilioaugust.copypus.data.SettingsViewModelFactory
import com.emilioaugust.copypus.ui.screens.MainScreen
import com.emilioaugust.copypus.ui.theme.ClipboardTheme
import com.emilioaugust.copypus.ui.theme.ThemeMode
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()
    private val settingsViewModel: SettingsViewModel by viewModels {
        SettingsViewModelFactory(
            SettingsDataStore(applicationContext)
        )
    }
    private lateinit var clipboardHelper: ClipboardManagerHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        clipboardHelper = ClipboardManagerHelper(this)
        setContent {
            val themeMode by settingsViewModel.themeMode.collectAsState(initial = ThemeMode.SYSTEM)
            val darkTheme = when(themeMode) {
                ThemeMode.DARK -> true
                ThemeMode.LIGHT -> false
                ThemeMode.SYSTEM ->
                    isSystemInDarkTheme()
            }
            ClipboardTheme(darkTheme = darkTheme) {
                MainScreen(viewModel, settingsViewModel)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            settingsViewModel.autoDelete.first().let { option ->
                viewModel.cleanupOldItems(option)
            }
            delay(250)
            clipboardHelper
                .getCurrentClipboardData()
                ?.let { data ->
                    when (data) {
                        is ClipboardData.Text -> {
                            viewModel.saveText(data.text)
                        }
                    }
                }
        }
    }
}