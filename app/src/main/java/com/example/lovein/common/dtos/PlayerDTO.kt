package com.example.lovein.common.dtos

import com.example.lovein.common.data.EroZone
import com.example.lovein.common.data.Gender
import kotlinx.serialization.Serializable

@Serializable
data class PlayerDTO(
    val name: String,
    val gender: Gender,
    val selectedEroZones: Set<EroZone>
)
