package com.example.lovein.createpartnerlist.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.lovein.common.models.Partner

@Composable
fun PartnerCard(
    partner: MutableState<Partner>,
    index: Int,
    partnerList: MutableList<MutableState<Partner>>
) {
    val isExpanded: MutableState<Boolean> = remember { mutableStateOf(false) }
    val alpha = animateFloatAsState(
        targetValue = if (isExpanded.value) 1f else 0f,
        animationSpec = tween(durationMillis = 1000)
    )
    val rotateX = animateFloatAsState(
        targetValue = if (isExpanded.value) 0f else -90f,
        animationSpec = tween(durationMillis = 1000)
    )

    PartnerInputRow(
        partnerList = partnerList,
        index = index,
        isExpanded = isExpanded
    )

    AnimatedVisibility(visible = isExpanded.value) {
        EroZoneListCard(
            partner = partner,
            rotateX = rotateX,
            alpha = alpha
        )
    }
}
