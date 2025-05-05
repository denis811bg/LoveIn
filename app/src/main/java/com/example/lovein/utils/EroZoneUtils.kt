package com.example.lovein.utils

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import com.example.lovein.common.data.EroZone
import com.example.lovein.common.models.EroZoneMutable

fun convertEroZoneToEroZoneMutable(eroZone: EroZone): EroZoneMutable {
    return EroZoneMutable(
        gender = eroZone.gender,
        resourceId = eroZone.resourceId,
        eroZoneType = eroZone.eroZoneType,
        actionList = eroZone.actionList.toMutableList()
    )
}

fun convertEroZoneMutableListToEroZoneList(selectedEroZoneMutableList: SnapshotStateList<EroZoneMutable>): List<EroZone> {
    return selectedEroZoneMutableList.mapNotNull { eroZoneMutable ->
        EroZone.entries.find { eroZone -> eroZone.resourceId == eroZoneMutable.resourceId }
    }
}

fun convertEroZoneListToEroZoneMutableList(eroZoneList: List<EroZone>): SnapshotStateList<EroZoneMutable> {
    return eroZoneList.map { eroZone ->
        EroZoneMutable(
            gender = eroZone.gender,
            resourceId = eroZone.resourceId,
            eroZoneType = eroZone.eroZoneType,
            actionList = eroZone.actionList.toMutableList()
        )
    }.toMutableStateList()
}
