package com.example.lovein.common.objects

import android.content.Context
import android.content.res.Configuration
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.util.Locale

object LocalizationManager {
    private var currentLocale: Locale by mutableStateOf(Locale("en"))

    fun setCurrentLocale(language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        currentLocale = locale
    }

    fun getLocalizedString(context: Context, resourceId: Int): String {
        val config = Configuration(context.resources.configuration)
        config.setLocale(currentLocale)

        val localizedContext = context.createConfigurationContext(config)
        return localizedContext.getString(resourceId)
    }
}
