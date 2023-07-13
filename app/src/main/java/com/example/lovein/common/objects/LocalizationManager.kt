package com.example.lovein.common.objects

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.util.*

object LocalizationManager {
    private var currentLocale: Locale by mutableStateOf(Locale("en"))

    fun setCurrentLocale(language: String, context: Context) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        currentLocale = locale
        updateConfiguration(context)
    }

    @SuppressLint("DiscouragedApi")
    fun getLocalizedString(context: Context, resourceId: Int): String {
        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(currentLocale)
        val localizedContext = context.createConfigurationContext(configuration)
        return localizedContext?.getString(resourceId) ?: context.resources.getString(resourceId)
    }

    private fun updateConfiguration(context: Context) {
        val resources = context.resources
        val configuration = Configuration(resources.configuration)
        configuration.setLocale(currentLocale)
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }
}
