package com.emilioaugust.copypus.data.tiles

import android.os.Build
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import androidx.annotation.RequiresApi
import com.emilioaugust.copypus.data.SettingsDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PauseMonitoringTileService : TileService() {
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
        qsTile?.apply {
            label = "Pause monitoring"
            subtitle =
                if (paused) {
                    "Paused"
                } else {
                    "Monitoring ON"
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