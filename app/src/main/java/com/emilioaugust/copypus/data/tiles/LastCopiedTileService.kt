package com.emilioaugust.copypus.data.tiles

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.emilioaugust.copypus.ClipboardData
import com.emilioaugust.copypus.data.AppDatabase
import com.emilioaugust.copypus.data.ClipboardRepository
import com.emilioaugust.copypus.data.SettingsDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LastCopiedTileService : TileService() {
    private lateinit var repository: ClipboardRepository

    override fun onCreate() {
        super.onCreate()
        val database = AppDatabase.getInstance(applicationContext)
        repository = ClipboardRepository(database.clipboardDao())
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onStartListening() {
        super.onStartListening()
        CoroutineScope(Dispatchers.IO).launch {
            val item = repository.getLatestItem()
            withContext(Dispatchers.Main) {
                updateTile(item?.text)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onClick() {
        super.onClick()
        CoroutineScope(Dispatchers.IO).launch {
            val item = repository.getLatestItem()
            if (item != null) {
                val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("Copied", item.text)
                clipboard.setPrimaryClip(clip)

                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        applicationContext,
                        "Copied",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun updateTile(text: String?) {
        qsTile?.apply {
            label = "Last Copied"
            subtitle =
                if (text.isNullOrBlank()) {
                    "No items"
                } else {
                    text.take(20)
                }
            state = Tile.STATE_ACTIVE
            updateTile()
        }
    }
}