package com.example.lovein.common.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

private var mInterstitialAd: InterstitialAd? = null
private var adUnitId = "ca-app-pub-3940256099942544/1033173712"

fun loadInterstitialAd(context: Context) {
    InterstitialAd.load(
        context,
        adUnitId,
        AdRequest.Builder().build(),
        object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.i("AD_TAG", "[onAdFailedToLoad]: Ad failed to load: ${adError.message}")
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                Log.i("AD_TAG", "[onAdLoaded] ${interstitialAd.adUnitId} ad loaded successfully.")
                mInterstitialAd = interstitialAd
            }
        }
    )
}

fun showInterstitialAd(context: Context) {
    val activity = context.findActivity()

    if (mInterstitialAd != null && activity != null) {
        mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                Log.i("AD_TAG", "[onAdFailedToShowFullScreenContent]: Ad failed to show: ${adError.message}")
                mInterstitialAd = null
            }

            override fun onAdDismissedFullScreenContent() {
                mInterstitialAd = null
                loadInterstitialAd(context)
            }
        }

        mInterstitialAd?.show(activity)
    }
}

fun removeInterstitialAd() {
    mInterstitialAd?.fullScreenContentCallback = null
    mInterstitialAd = null
}

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}
