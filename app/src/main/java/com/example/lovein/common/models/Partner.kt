package com.example.lovein.common.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.lovein.common.data.Gender
import com.example.lovein.ui.theme.FemaleColor
import com.example.lovein.ui.theme.MaleColor

class Partner(
    var name: MutableState<String> = mutableStateOf(""),
    var gender: MutableState<Gender> = mutableStateOf(Gender.MALE),
    var selectedEroZones: SnapshotStateList<EroZoneMutable> = mutableStateListOf(),
    var icon: MutableState<ImageVector> =
        mutableStateOf(
            if (gender.value == Gender.MALE)
                Icons.Default.Male
            else
                Icons.Default.Female
        ),
    var color: MutableState<Color> =
        mutableStateOf(
            if (gender.value == Gender.MALE)
                MaleColor
            else
                FemaleColor
        )
) {
    constructor(gender: Gender) : this(gender = mutableStateOf(gender))
}
