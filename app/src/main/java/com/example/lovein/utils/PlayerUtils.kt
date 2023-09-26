package com.example.lovein.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.lovein.common.data.Gender
import com.example.lovein.common.dtos.PlayerDTO
import com.example.lovein.common.models.Player
import com.example.lovein.ui.theme.FemaleColor
import com.example.lovein.ui.theme.MaleColor

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
