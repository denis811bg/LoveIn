package com.example.lovein

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.example.lovein.common.utils.loadInterstitialAd
import com.example.lovein.common.utils.removeInterstitialAd
import com.example.lovein.navigation.LoveInNavigation
import com.example.lovein.ui.theme.LoveInTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            LoveInTheme {
                LoveInNavigation()
            }
        }

        loadInterstitialAd(this)
    }

    override fun onDestroy() {
        removeInterstitialAd()
        super.onDestroy()
    }
}
