package com.example.lovein.createpartnerlist.validation

import androidx.compose.runtime.MutableState
import com.example.lovein.R
import com.example.lovein.common.models.Partner

data class ValidationResult(
    val isValid: Boolean,
    val titleResId: Int? = null,
    val textResId: Int? = null
)

fun validatePartners(partnerList: List<MutableState<Partner>>): ValidationResult {
    return when {
        partnerList.size < 2 -> ValidationResult(
            isValid = false,
            titleResId = R.string.not_enough_partners_alert_title,
            textResId = R.string.not_enough_partners_alert_description
        )

        partnerList.any { it.value.name.value.isBlank() } -> ValidationResult(
            isValid = false,
            titleResId = R.string.add_partner_name_alert_title,
            textResId = R.string.add_partner_name_alert_description
        )

        partnerList.any { it.value.selectedEroZones.isEmpty() } -> ValidationResult(
            isValid = false,
            titleResId = R.string.add_partner_ero_zones_alert_title,
            textResId = R.string.add_partner_ero_zones_alert_description
        )

        else -> ValidationResult(isValid = true)
    }
}