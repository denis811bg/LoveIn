package com.example.lovein.utils

import android.content.Context
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.lovein.common.data.Gender
import com.example.lovein.common.dtos.PartnerDTO
import com.example.lovein.common.models.FeedbackType
import com.example.lovein.common.models.Partner
import com.example.lovein.common.objects.LocalizationManager
import com.example.lovein.createpartnerlist.validation.ValidationResult
import com.example.lovein.ui.theme.FemaleColor
import com.example.lovein.ui.theme.MaleColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.random.Random

fun convertPartnerListToPartnerDTOList(partnerList: MutableList<MutableState<Partner>>): List<PartnerDTO> {
    return partnerList.map { partner ->
        PartnerDTO(
            name = partner.value.name.value,
            gender = partner.value.gender.value,
            selectedEroZoneList = convertEroZoneMutableListToEroZoneList(partner.value.selectedEroZones)
        )
    }.toList()
}

fun convertPartnerDTOListToPartnerList(partnerDTOList: List<PartnerDTO>): List<Partner> {
    return partnerDTOList.map { partnerDTO ->
        Partner(
            name = mutableStateOf(partnerDTO.name),
            gender = mutableStateOf(partnerDTO.gender),
            selectedEroZones = convertEroZoneListToEroZoneMutableList(partnerDTO.selectedEroZoneList),
            icon = mutableStateOf(
                if (partnerDTO.gender == Gender.MALE)
                    Icons.Default.Male
                else
                    Icons.Default.Female
            ),
            color = mutableStateOf(
                if (partnerDTO.gender == Gender.MALE)
                    MaleColor
                else
                    FemaleColor
            )
        )
    }
}

fun addRandomPartner(
    partnerList: MutableList<MutableState<Partner>>,
    coroutineScope: CoroutineScope,
    listState: LazyListState
) {
    val gender = if (Random.nextBoolean()) Gender.MALE else Gender.FEMALE
    partnerList.add(mutableStateOf(Partner(gender)))

    coroutineScope.launch {
        listState.animateScrollToItem(partnerList.size - 1)
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

fun cleanupActionFeedback(partnerList: MutableList<MutableState<Partner>>) {
    partnerList.forEach { partner ->
        partner.value.selectedEroZones.forEach { eroZone ->
            eroZone.actionList.forEach { it.feedback.value = FeedbackType.NONE }
        }
    }
}

fun formatPartnerName(input: String): String {
    val endsWithSpace = input.endsWith(' ')
    val parts = input.trim().lowercase()
        .split(Regex("\\s+"))
        .filter { it.isNotBlank() }

    val formatted = parts.joinToString(" ") { word ->
        word.replaceFirstChar { it.titlecase() }
    }

    return if (endsWithSpace) "$formatted " else formatted
}
