package com.example.lovein.common.models

import com.example.lovein.common.data.EroZoneType
import com.example.lovein.common.data.Gender

class EroZoneMutable(
    var gender: List<Gender>,
    var resourceId: Int,
    var eroZoneType: EroZoneType,
    var actionList: MutableList<ActionWithFeedback>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EroZoneMutable

        if (gender != other.gender) return false
        if (resourceId != other.resourceId) return false
        if (eroZoneType != other.eroZoneType) return false
        if (actionList != other.actionList) return false

        return true
    }

    override fun hashCode(): Int {
        var result: Int = gender.hashCode()

        result = 31 * result + resourceId
        result = 31 * result + eroZoneType.hashCode()
        result = 31 * result + actionList.hashCode()

        return result
    }
}
