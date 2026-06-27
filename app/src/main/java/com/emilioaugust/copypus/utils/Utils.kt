package com.emilioaugust.copypus.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


// FORMAT TIME

fun formatTime(timestamp: Long): String {
    val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    return formatter.format(Date(timestamp))
}

fun formatSectionTitle(timestamp: Long): String {
    val now = Calendar.getInstance()
    val itemDate = Calendar.getInstance()

    itemDate.timeInMillis = timestamp
    return when {
        isSameDay(now, itemDate) -> {"Today"}
        isYesterday(now, itemDate) -> {"Yesterday"}
        else -> {
            SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(timestamp))
        }
    }
}

fun isSameDay(first: Calendar, second: Calendar): Boolean {
    return first.get(Calendar.YEAR) == second.get(Calendar.YEAR)
            && first.get(Calendar.DAY_OF_YEAR) == second.get(Calendar.DAY_OF_YEAR)
}

fun isYesterday(today: Calendar, itemDate: Calendar): Boolean {
    val yesterday = Calendar.getInstance()
    yesterday.add(Calendar.DAY_OF_YEAR, -1)
    return isSameDay(yesterday, itemDate)
}

// TYPE

enum class ClipboardType {
    LINK,
    CODE,
    TEXT
}

fun detectClipboardType(text: String): ClipboardType {
    val lower = text.lowercase()
    val isLink =
        lower.startsWith("http://") || lower.startsWith("https://") ||
                lower.startsWith("www.")

    if (isLink) {
        return ClipboardType.LINK
    }

    val codeKeywords = listOf(
        "fun ", "class ", "val ", "var ", "const ", "import ",
        "public ", "private ", "return ", "if(", "if (", "else",
        "{", "}", ";", "<?php", "console.log", "println", "System.out",
        "#include", "def ", "print(", "SELECT ", "INSERT ", "UPDATE "
    )

    val isCode = codeKeywords.any { lower.contains(it.lowercase()) }

    if (isCode) {
        return ClipboardType.CODE
    }

    return ClipboardType.TEXT
}