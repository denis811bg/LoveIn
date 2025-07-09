package com.example.lovein.createplayerlist.validation

import androidx.compose.runtime.MutableState
import com.example.lovein.R
import com.example.lovein.common.models.Player

data class ValidationResult(
    val isValid: Boolean,
    val titleResId: Int? = null,
    val textResId: Int? = null
)

fun validatePlayers(playerList: List<MutableState<Player>>): ValidationResult {
    return when {
        playerList.size < 2 -> ValidationResult(
            isValid = false,
            titleResId = R.string.not_enough_players_alert_title,
            textResId = R.string.not_enough_players_alert_description
        )
        playerList.any { it.value.name.value.isBlank() } -> ValidationResult(
            isValid = false,
            titleResId = R.string.add_player_names_alert_title,
            textResId = R.string.add_player_names_alert_description
        )
        playerList.any { it.value.selectedEroZones.isEmpty() } -> ValidationResult(
            isValid = false,
            titleResId = R.string.add_player_ero_zones_alert_title,
            textResId = R.string.add_player_ero_zones_alert_description
        )
        else -> ValidationResult(isValid = true)
    }
}