package com.example.lovein.createplayerlist.utils

import android.content.Context
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.lovein.common.data.Gender
import com.example.lovein.common.models.Player
import com.example.lovein.common.objects.LocalizationManager
import com.example.lovein.createplayerlist.validation.ValidationResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.random.Random

fun addRandomPlayer(
    playerList: MutableList<MutableState<Player>>,
    coroutineScope: CoroutineScope,
    listState: LazyListState
) {
    val gender = if (Random.nextBoolean()) Gender.MALE else Gender.FEMALE
    playerList.add(mutableStateOf(Player(gender)))

    coroutineScope.launch {
        listState.animateScrollToItem(playerList.size - 1)
    }
}

fun showAlertDialog(
    context: Context,
    result: ValidationResult,
    isAlertDialogOpen: MutableState<Boolean>,
    alertDialogTitle: MutableState<String>,
    alertDialogText: MutableState<String>
) {
    isAlertDialogOpen.value = true
    alertDialogTitle.value = LocalizationManager.getLocalizedString(context, result.titleResId!!)
    alertDialogText.value = LocalizationManager.getLocalizedString(context, result.textResId!!)
}
