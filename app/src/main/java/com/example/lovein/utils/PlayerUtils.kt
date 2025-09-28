package com.example.lovein.utils

import android.content.Context
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.lovein.common.data.Gender
import com.example.lovein.common.dtos.PlayerDTO
import com.example.lovein.common.models.FeedbackType
import com.example.lovein.common.models.Player
import com.example.lovein.common.objects.LocalizationManager
import com.example.lovein.createplayerlist.validation.ValidationResult
import com.example.lovein.ui.theme.FemaleColor
import com.example.lovein.ui.theme.MaleColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.random.Random

fun convertPlayerListToPlayerDTOList(playerList: MutableList<MutableState<Player>>): List<PlayerDTO> {
    return playerList.map { player ->
        PlayerDTO(
            name = player.value.name.value,
            gender = player.value.gender.value,
            selectedEroZoneList = convertEroZoneMutableListToEroZoneList(player.value.selectedEroZones)
        )
    }.toList()
}

fun convertPlayerDTOListToPlayerList(playerDTOList: List<PlayerDTO>): List<Player> {
    return playerDTOList.map { playerDTO ->
        Player(
            name = mutableStateOf(playerDTO.name),
            gender = mutableStateOf(playerDTO.gender),
            selectedEroZones = convertEroZoneListToEroZoneMutableList(playerDTO.selectedEroZoneList),
            icon = mutableStateOf(
                if (playerDTO.gender == Gender.MALE)
                    Icons.Default.Male
                else
                    Icons.Default.Female
            ),
            color = mutableStateOf(
                if (playerDTO.gender == Gender.MALE)
                    MaleColor
                else
                    FemaleColor
            )
        )
    }
}

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

fun cleanupActionFeedback(playerList: MutableList<MutableState<Player>>) {
    playerList.forEach { player ->
        player.value.selectedEroZones.forEach { eroZone ->
            eroZone.actionList.forEach { it.feedback.value = FeedbackType.NONE }
        }
    }
}

fun formatName(input: String): String {
    return input
        .lowercase()
        .split(" ")
        .filter { it.isNotBlank() }
        .joinToString(" ") { word ->
            word.replaceFirstChar { c -> c.titlecase() }
        }
}
