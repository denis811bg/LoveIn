package com.example.lovein.common.objects

import android.content.Context
import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.lovein.common.data.Language
import java.util.Locale

object LocalizationManager {
    private var currentLocale: Locale by mutableStateOf(Locale(Language.ENGLISH.langAbbrev))

    fun setCurrentLocale(language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        currentLocale = locale
    }

    fun getLocalizedString(context: Context, @StringRes resourceId: Int, vararg args: Any): String {
        val config = Configuration(context.resources.configuration)
        config.setLocale(currentLocale)

        val localizedContext = context.createConfigurationContext(config)
        return if (args.isNotEmpty()) localizedContext.getString(
            resourceId,
            *args
        ) else localizedContext.getString(resourceId)
    }
}
