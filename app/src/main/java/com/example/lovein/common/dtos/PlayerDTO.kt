package com.example.lovein.common.dtos

import android.os.Parcelable
import com.example.lovein.common.data.EroZone
import com.example.lovein.common.data.Gender
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlayerDTO(
    val name: String,
    val gender: Gender,
    val selectedEroZoneList: List<EroZone>
): Parcelable
