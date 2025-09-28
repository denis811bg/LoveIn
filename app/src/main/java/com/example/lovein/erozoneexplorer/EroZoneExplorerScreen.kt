package com.example.lovein.erozoneexplorer

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.lovein.R
import com.example.lovein.common.components.CommonContainer
import com.example.lovein.common.components.CommonNavigationButton
import com.example.lovein.common.components.CustomAlertDialog
import com.example.lovein.common.constants.CommonConstants
import com.example.lovein.common.constants.DescriptionConstants
import com.example.lovein.common.data.ActionType
import com.example.lovein.common.data.EroZone
import com.example.lovein.common.data.EroZoneType
import com.example.lovein.common.data.Gender
import com.example.lovein.common.data.NavigationScreens
import com.example.lovein.common.dtos.PartnerDTO
import com.example.lovein.common.models.ActionWithFeedback
import com.example.lovein.common.models.EroZoneMutable
import com.example.lovein.common.models.Partner
import com.example.lovein.common.objects.LocalizationManager
import com.example.lovein.erozoneexplorer.components.Stack
import com.example.lovein.erozoneexplorer.dataclass.ZoneCounter
import com.example.lovein.erozoneexplorer.models.Card
import com.example.lovein.erozoneexplorer.models.CardBack
import com.example.lovein.erozoneexplorer.models.CardFront
import com.example.lovein.ui.theme.BackgroundLightBlueColor
import com.example.lovein.ui.theme.BackgroundLightPinkColor
import com.example.lovein.ui.theme.FemaleColor
import com.example.lovein.ui.theme.MaleColor
import com.example.lovein.utils.convertPartnerDTOListToPartnerList
import com.example.lovein.utils.generateFeedbackReport
import com.example.lovein.utils.saveFeedbackReportToPdf

@Composable
fun EroZoneExplorerScreen(
    navController: NavController,
    partnerDTOList: List<PartnerDTO>
) {
    val context: Context = LocalContext.current

    val snackbarHostState = remember { SnackbarHostState() }

    val partnerList: List<Partner> = convertPartnerDTOListToPartnerList(partnerDTOList)

    val partnerIndex: MutableState<Int> = remember { mutableIntStateOf(0) }
    val actionCards: List<Pair<Partner, Card>> = initActionCards(context, partnerList)

    val isAlertDialogOpen: MutableState<Boolean> = remember { mutableStateOf(false) }
    val alertDialogTitle: MutableState<String> = remember { mutableStateOf("") }
    val alertDialogText: MutableState<String> = remember { mutableStateOf("") }

    CommonContainer(
        navController = navController,
        snackbarHostState = snackbarHostState
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 16.dp,
                    top = innerPadding.calculateTopPadding(),
                    end = 16.dp,
                    bottom = 16.dp
                ),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(9f)
                ) {
                    Stack(
                        cards = actionCards,
                        position = partnerIndex.value
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    CommonNavigationButton(
                        text = LocalizationManager.getLocalizedString(
                            context = context,
                            resourceId = R.string.open_next_card
                        ),
                        icon = Icons.Default.PlayCircle,
                        iconContentDescription = DescriptionConstants.PLAY_CIRCLE_ICON,
                        onClick = {
                            partnerIndex.value++

                            if (partnerIndex.value >= actionCards.size) {
                                isAlertDialogOpen.value = true
                                alertDialogTitle.value = LocalizationManager.getLocalizedString(
                                    context = context,
                                    resourceId = R.string.finish_title
                                )
                                alertDialogText.value = LocalizationManager.getLocalizedString(
                                    context = context,
                                    resourceId = R.string.finish_description
                                )
                            }
                        },
                        modifier = Modifier.align(alignment = Alignment.BottomCenter)
                    )
                }
            }
        }
    }

    if (isAlertDialogOpen.value) {
        CustomAlertDialog(
            isAlertDialogOpen = isAlertDialogOpen,
            title = alertDialogTitle.value,
            text = alertDialogText.value,
            navController = navController,
            onDownloadClick = {
                val report = generateFeedbackReport(context, actionCards)
                val result = saveFeedbackReportToPdf(context, report)

                val message = "Report saved in ${result.directoryLabel}/${result.filename}"

                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set(CommonConstants.SNACKBAR_MESSAGE, message)

                isAlertDialogOpen.value = false

                navController.popBackStack(
                    route = NavigationScreens.CREATE_PARTNER_LIST_SCREEN.name,
                    inclusive = false
                )
            }
        )
    }
}

@Preview
@Composable
fun EroZoneExplorerScreenPreview() {
    val navController: NavController = rememberNavController()
    val partnerDTOLists: List<PartnerDTO> =
        listOf(
            PartnerDTO(
                "Denis",
                Gender.MALE,
                listOf(
                    EroZone.SCALP_AND_HAIR,
                    EroZone.ARMPITS,
                    EroZone.NIPPLES,
                    EroZone.NAVEL,
                    EroZone.PROSTATE,
                    EroZone.PERINEUM
                )
            ),
            PartnerDTO(
                "Alena",
                Gender.FEMALE,
                listOf(
                    EroZone.EARS,
                    EroZone.NECK,
                    EroZone.LOWER_STOMACH,
                    EroZone.PUBIC_MOUND,
                    EroZone.CLITORIS,
                    EroZone.ANUS
                )
            ),
            PartnerDTO(
                "Sofi",
                Gender.FEMALE,
                listOf(
                    EroZone.INNER_WRIST,
                    EroZone.INNER_ARMS,
                    EroZone.AREOLA_AND_NIPPLES,
                    EroZone.BUTTOCKS,
                    EroZone.G_SPOT,
                )
            )
        )

    EroZoneExplorerScreen(
        navController = navController,
        partnerDTOList = partnerDTOLists
    )
}

private fun initActionCards(
    context: Context,
    partnerList: List<Partner>
): MutableList<Pair<Partner, Card>> {
    val actionCards = mutableListOf<Pair<Partner, Card>>()
    if (partnerList.isEmpty()) return actionCards

    val priority = listOf(EroZoneType.SOFT, EroZoneType.HOT, EroZoneType.HARD)

    val countersByPartner: List<MutableList<ZoneCounter>> = partnerList.map { partner ->
        partner.selectedEroZones
            .filter { it.actionList.isNotEmpty() }
            .map { ZoneCounter(it, it.actionList.size) }
            .toMutableList()
    }

    val totalActions = countersByPartner.sumOf { zones -> zones.sumOf { it.remaining } }
    if (totalActions == 0) return actionCards

    var partnerIndex = 1
    var produced = 0
    var spinsWithoutCard = 0
    val maxSpins = partnerList.size * 2

    while (produced < totalActions && spinsWithoutCard < maxSpins) {
        val passiveIdx = partnerIndex % partnerList.size
        val activeIdx = (partnerIndex - 1) % partnerList.size

        val zone = nextZoneForPartner(countersByPartner[passiveIdx], priority)

        if (zone != null) {
            zone.remaining--

            val card = card(
                context = context,
                eroZoneMutable = zone.zone,
                activePartner = partnerList[activeIdx],
                passivePartner = partnerList[passiveIdx]
            )

            actionCards.add(partnerList[passiveIdx] to card)
            produced++
            spinsWithoutCard = 0
        } else {
            spinsWithoutCard++
        }

        partnerIndex++
    }

    return actionCards
}

private fun nextZoneForPartner(
    counters: MutableList<ZoneCounter>,
    priority: List<EroZoneType>
): ZoneCounter? {
    counters.removeAll { it.remaining <= 0 }

    for (type in priority) {
        val candidates = counters.filter { it.remaining > 0 && it.zone.eroZoneType == type }
        if (candidates.isNotEmpty()) return candidates.random()
    }
    return null
}

@Suppress("UNREACHABLE_CODE")
private fun card(
    context: Context,
    eroZoneMutable: EroZoneMutable,
    activePartner: Partner,
    passivePartner: Partner
): Card {
    val actionWithFeedback = getAction(eroZoneMutable) ?: return error("No action")

    val cardFrontContent = buildStylizedText(
        simpleText = LocalizationManager.getLocalizedString(
            context = context,
            resourceId = actionWithFeedback.action.descriptionResId
        ).replaceFirst("%", activePartner.name.value)
            .replaceFirst("%", passivePartner.name.value),
        stylizedTexts = listOf(activePartner.name.value, passivePartner.name.value),
        cardColor = if (passivePartner.gender.value == Gender.MALE) MaleColor else FemaleColor
    )

    return createCard(
        cardFrontContent = cardFrontContent,
        gender = passivePartner.gender.value,
        actionWithFeedback = actionWithFeedback
    )
}

private fun createCard(
    cardFrontContent: AnnotatedString,
    gender: Gender,
    actionWithFeedback: ActionWithFeedback
): Card {
    return Card(
        CardFront(
            content = cardFrontContent,
            color = setBackgroundColor(gender),
            actionWithFeedback = actionWithFeedback
        ),
        CardBack(color = setBackgroundColor(gender))
    )
}

private fun getAction(eroZoneMutable: EroZoneMutable): ActionWithFeedback? {
    var action: ActionWithFeedback? = eroZoneMutable.actionList
        .filter { it.action.type == ActionType.SOFT }
        .randomOrNull()

    if (action != null) {
        eroZoneMutable.actionList.remove(action)
        return action
    }

    action = eroZoneMutable.actionList
        .filter { it.action.type == ActionType.HOT }
        .randomOrNull()
    if (action != null) {
        eroZoneMutable.actionList.remove(action)
        return action
    }

    return null
}

private fun setBackgroundColor(gender: Gender): Color {
    return if (gender == Gender.MALE)
        MaleColor
    else
        FemaleColor
}

private fun buildStylizedText(
    simpleText: String,
    stylizedTexts: List<String>,
    cardColor: Color
): AnnotatedString {
    var remainingText = simpleText
    return buildAnnotatedString {
        for (stylizedText in stylizedTexts) {
            val startIndex = remainingText.indexOf(stylizedText)
            if (startIndex >= 0) {
                val endIndex = startIndex + stylizedText.length
                append(remainingText.substring(0, startIndex))

                pushStyle(
                    SpanStyle(
                        color = if (cardColor == MaleColor) BackgroundLightBlueColor else BackgroundLightPinkColor
                    )
                )
                append(remainingText.substring(startIndex, endIndex))
                pop()

                remainingText = remainingText.substring(endIndex)
            }
        }
        append(remainingText)
    }
}
