package com.emilioaugust.copypus.utils

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

object LocaleHelper {
    fun setLocale(context: Context, lang: String): Context {
        val locale = Locale(lang)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)

        return context.createConfigurationContext(config)
    }
}