package com.emilioaugust.copypus.data

import androidx.annotation.StringRes
import com.emilioaugust.copypus.R

enum class AppLanguage(val code: String, @StringRes val titleRes: Int) {
    SYSTEM("", R.string.system_lng),
    ENGLISH("en", R.string.english_lng),
    RUSSIAN("ru", R.string.russian_lng),
    SPANISH("es", R.string.spanish_lng)
}