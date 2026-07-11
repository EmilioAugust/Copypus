package com.emilioaugust.copypus.data.tiles

import android.os.Build
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import androidx.annotation.RequiresApi
import com.emilioaugust.copypus.R
import com.emilioaugust.copypus.data.SettingsDataStore
import com.emilioaugust.copypus.utils.LocaleHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PauseMonitoringTileService : TileService() {
    private val settingsDataStore by lazy { SettingsDataStore(applicationContext) }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onStartListening() {
        super.onStartListening()
        CoroutineScope(Dispatchers.IO).launch {
            val dataStore = SettingsDataStore(applicationContext)
            val enabled = dataStore.monitoringEnabledFlow.first()
            withContext(Dispatchers.Main) {
                updateTile(!enabled)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onClick() {
        super.onClick()
        CoroutineScope(Dispatchers.IO).launch {
            val dataStore = SettingsDataStore(applicationContext)

            val enabled = dataStore.monitoringEnabledFlow.first()

            if (enabled) {
                val duration = dataStore.pauseDurationFlow.first()
                dataStore.pauseMonitoring(duration)
                withContext(Dispatchers.Main) {
                    updateTile(true)
                }

            } else {
                dataStore.setMonitoringEnabled(true)
                withContext(Dispatchers.Main) {
                    updateTile(false)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun updateTile(paused: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            val language = settingsDataStore.language.first()
            val context = LocaleHelper.setLocale(applicationContext, language.code)
            qsTile?.apply {
                label = context.getString(R.string.pause_monitoring_tile_name)
                subtitle =
                    if (paused) {
                        context.getString(R.string.paused_subtitle_tile)
                    } else {
                        context.getString(R.string.monitoring_on_subtitle_tile)
                    }

                state =
                    if (paused) {
                        Tile.STATE_ACTIVE
                    } else {
                        Tile.STATE_INACTIVE
                    }
                updateTile()
            }
        }
    }
}