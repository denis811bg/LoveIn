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
import com.example.lovein.common.data.ActionType
import com.example.lovein.common.data.EroZone
import com.example.lovein.common.data.EroZoneType
import com.example.lovein.common.data.Gender
import com.example.lovein.common.data.NavigationScreens
import com.example.lovein.common.dtos.PlayerDTO
import com.example.lovein.common.models.ActionWithFeedback
import com.example.lovein.common.models.EroZoneMutable
import com.example.lovein.common.models.Player
import com.example.lovein.common.objects.LocalizationManager
import com.example.lovein.erozoneexplorer.components.Stack
import com.example.lovein.erozoneexplorer.models.Card
import com.example.lovein.erozoneexplorer.models.CardBack
import com.example.lovein.erozoneexplorer.models.CardFront
import com.example.lovein.ui.theme.BackgroundLightBlueColor
import com.example.lovein.ui.theme.BackgroundLightPinkColor
import com.example.lovein.ui.theme.FemaleColor
import com.example.lovein.ui.theme.MaleColor
import com.example.lovein.utils.convertPlayerDTOListToPlayerList
import com.example.lovein.utils.generateFeedbackReport
import com.example.lovein.utils.saveFeedbackReportToPdf

@Composable
fun EroZoneExplorerScreen(
    navController: NavController,
    playerDTOList: List<PlayerDTO>
) {
    val context: Context = LocalContext.current

    val snackbarHostState = remember { SnackbarHostState() }

    val playerList: List<Player> = convertPlayerDTOListToPlayerList(playerDTOList)

    val playerIndex: MutableState<Int> = remember { mutableIntStateOf(0) }
    val actionCards: List<Pair<Player, Card>> = initActionCards(context, playerList)

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
                        position = playerIndex.value
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
                            resourceId = R.string.play_button
                        ),
                        icon = Icons.Default.PlayCircle,
                        iconContentDescription = "play_circle_icon",
                        onClick = {
                            playerIndex.value++

                            if (playerIndex.value >= actionCards.size) {
                                isAlertDialogOpen.value = true
                                alertDialogTitle.value = LocalizationManager.getLocalizedString(
                                    context = context,
                                    resourceId = R.string.game_over_title
                                )
                                alertDialogText.value = LocalizationManager.getLocalizedString(
                                    context = context,
                                    resourceId = R.string.game_over_description
                                )
                                    .replaceFirst("%", getPlayerNameWithEmptyActions(playerList))
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
                    route = NavigationScreens.CREATE_PLAYER_LIST_SCREEN.name,
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
    val playerDTOList: List<PlayerDTO> =
        listOf(
            PlayerDTO(
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
            PlayerDTO(
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
            PlayerDTO(
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
        playerDTOList = playerDTOList
    )
}

private fun initActionCards(
    context: Context,
    playerList: List<Player>
): MutableList<Pair<Player, Card>> {
    val actionCards: MutableList<Pair<Player, Card>> = mutableListOf()

    var playerIndex = 1
    while (true) {
        if (playerList.isEmpty()) break

        val passivePlayerIndex = playerIndex % playerList.size
        val activePlayerIndex = (playerIndex - 1) % playerList.size

        val selectedEroZoneMutableList =
            playerList[passivePlayerIndex].selectedEroZones.toMutableList()
        val eroZoneMutable: EroZoneMutable? = getEroZone(selectedEroZoneMutableList)

        if (eroZoneMutable != null) {
            val card = card(
                context = context,
                eroZoneMutable = eroZoneMutable,
                activePlayer = playerList[activePlayerIndex],
                passivePlayer = playerList[passivePlayerIndex]
            )
            actionCards.add(playerList[passivePlayerIndex] to card)
            playerIndex++
        } else break
    }

    return actionCards
}

private fun getEroZone(selectedEroZoneMutableList: MutableList<EroZoneMutable>): EroZoneMutable? {
    var eroZoneMutable: EroZoneMutable? =
        selectedEroZoneMutableList
            .filter { it.eroZoneType == EroZoneType.SOFT }
            .randomOrNull()

    if (eroZoneMutable != null) {
        if (eroZoneMutable.actionList.isNotEmpty()) {
            return eroZoneMutable
        } else {
            selectedEroZoneMutableList.remove(eroZoneMutable)
            getEroZone(selectedEroZoneMutableList)
        }
    }

    eroZoneMutable =
        selectedEroZoneMutableList.filter { it.eroZoneType == EroZoneType.HOT }.randomOrNull()
    if (eroZoneMutable != null) {
        if (eroZoneMutable.actionList.isNotEmpty()) {
            return eroZoneMutable
        } else {
            selectedEroZoneMutableList.remove(eroZoneMutable)
            getEroZone(selectedEroZoneMutableList)
        }
    }

    eroZoneMutable =
        selectedEroZoneMutableList.filter { it.eroZoneType == EroZoneType.HARD }.randomOrNull()
    if (eroZoneMutable != null) {
        if (eroZoneMutable.actionList.isNotEmpty()) {
            return eroZoneMutable
        } else {
            selectedEroZoneMutableList.remove(eroZoneMutable)
            getEroZone(selectedEroZoneMutableList)
        }
    }

    return null
}

@Suppress("UNREACHABLE_CODE")
private fun card(
    context: Context,
    eroZoneMutable: EroZoneMutable,
    activePlayer: Player,
    passivePlayer: Player
): Card {
    val actionWithFeedback = getAction(eroZoneMutable) ?: return error("No action")

    val cardFrontContent = buildStylizedText(
        simpleText = LocalizationManager.getLocalizedString(
            context = context,
            resourceId = actionWithFeedback.action.descriptionResId
        ).replaceFirst("%", activePlayer.name.value)
            .replaceFirst("%", passivePlayer.name.value),
        stylizedTexts = listOf(activePlayer.name.value, passivePlayer.name.value),
        cardColor = if (passivePlayer.gender.value == Gender.MALE) MaleColor else FemaleColor
    )

    return createCard(
        cardFrontContent = cardFrontContent,
        gender = passivePlayer.gender.value,
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

private fun getPlayerNameWithEmptyActions(playerList: List<Player>): String {
    val playerWithEmptyActions: Player? = playerList.firstOrNull { player ->
        player.selectedEroZones.any { eroZoneMutable ->
            eroZoneMutable.actionList.isEmpty()
        }
    }
    return playerWithEmptyActions?.name?.value ?: ""
}
