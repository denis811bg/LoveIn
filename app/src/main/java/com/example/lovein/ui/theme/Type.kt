package com.example.lovein.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.example.lovein.R

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)

val montserratFont = GoogleFont(name = "Montserrat")
val helveticaFont = GoogleFont(name = "Helvetica")
val googleProvider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)
val montserratFontFamily = FontFamily(
    Font(
        googleFont = montserratFont,
        fontProvider = googleProvider,
        weight = FontWeight.W800,
        style = FontStyle.Italic
    )
)

val helveticaFontFamily = FontFamily(
    Font(
        googleFont = helveticaFont,
        fontProvider = googleProvider
    )
)
