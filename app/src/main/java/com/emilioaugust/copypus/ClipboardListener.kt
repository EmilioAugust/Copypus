package com.emilioaugust.copypus

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.emilioaugust.copypus.data.SettingsDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


sealed class ClipboardData {
    data class Text(
        val text: String
    ) : ClipboardData()
}

class ClipboardManagerHelper(val appContext: Context) {
    private val clipboardManager = appContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    private var listener: ClipboardManager.OnPrimaryClipChangedListener? = null
    private var ignoreNext = false
    private val settingsDataStore = SettingsDataStore(appContext)

    fun getCurrentClipboardData(): ClipboardData? {
        return try {
            val clip = clipboardManager.primaryClip
            val item = clip?.getItemAt(0)
            if (item != null) {
                val text = item
                    .coerceToText(appContext)
                    ?.toString()
                if (!text.isNullOrBlank()) {
                    return ClipboardData.Text(text)
                }
            }
            null

        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun startListening(onNewData: (ClipboardData) -> Unit) {
        listener = ClipboardManager.OnPrimaryClipChangedListener {
                CoroutineScope(Dispatchers.IO).launch {
                    val enabled = settingsDataStore.monitoringEnabledFlow.first()
                    if (!enabled) return@launch

                    val clip = clipboardManager.primaryClip
                    val item = clip?.getItemAt(0)

                    if (item != null) {
                        val text = item.coerceToText(appContext)?.toString()
                        if (!text.isNullOrBlank()) {
                            withContext(Dispatchers.Main) {
                                onNewData(ClipboardData.Text(text))
                            }
                        }
                    }
                }
            }
        clipboardManager.addPrimaryClipChangedListener(listener)
    }

    fun stopListening() {
        listener?.let {
            clipboardManager.removePrimaryClipChangedListener(it)
        }
    }

    fun copyTextToClipboard(text: String?) {
        ignoreNext = true
        val clip = ClipData.newPlainText("Saved text", text)
        clipboardManager.setPrimaryClip(clip)
    }
}