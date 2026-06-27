package com.emilioaugust.copypus.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(tableName = "clipboard_items", indices = [Index(value = ["text"], unique = true)])
data class ClipboardItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val text: String,
    val isFavorite: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)
