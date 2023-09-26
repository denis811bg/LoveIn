package com.example.lovein.erozoneexplorer

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.runtime.*
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
import com.example.lovein.common.data.*
import com.example.lovein.common.dtos.PlayerDTO
import com.example.lovein.common.models.EroZoneMutable
import com.example.lovein.common.models.Player
import com.example.lovein.common.objects.LocalizationManager
import com.example.lovein.common.utils.showInterstitialAd
import com.example.lovein.createplayerlist.components.CustomAlertDialog
import com.example.lovein.erozoneexplorer.components.Stack
import com.example.lovein.erozoneexplorer.models.Card
import com.example.lovein.erozoneexplorer.models.CardBack
import com.example.lovein.erozoneexplorer.models.CardFront
import com.example.lovein.ui.theme.BackgroundLightBlueColor
import com.example.lovein.ui.theme.BackgroundLightPinkColor
import com.example.lovein.ui.theme.FemaleColor
import com.example.lovein.ui.theme.MaleColor
import com.example.lovein.utils.convertPlayerDTOListToPlayerList

@Composable
fun EroZoneExplorerScreen(
    navController: NavController,
    playerDTOList: List<PlayerDTO>
) {
    val context: Context = LocalContext.current

    val playerList: List<Player> = convertPlayerDTOListToPlayerList(playerDTOList)

    val playerIndex: MutableIntState = remember { mutableStateOf(0) }
    val actionCards: MutableList<Card> = initActionCards(context, playerList)

    val isAlertDialogOpen: MutableState<Boolean> = remember { mutableStateOf(false) }
    val alertDialogTitle: MutableState<String> = remember { mutableStateOf("") }
    val alertDialogText: MutableState<String> = remember { mutableStateOf("") }

    CommonContainer(navController = navController) { innerPadding ->
        if (playerIndex.intValue != 0 && playerIndex.intValue % 5 == 0) showInterstitialAd(context)

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
                        position = playerIndex.intValue
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
                            playerIndex.intValue++

                            if (playerIndex.intValue >= actionCards.size) {
                                isAlertDialogOpen.value = true
                                alertDialogTitle.value = LocalizationManager.getLocalizedString(
                                    context = context,
                                    resourceId = R.string.game_over_title
                                )
                                alertDialogText.value = LocalizationManager.getLocalizedString(
                                    context = context,
                                    resourceId = R.string.game_over_description
                                )
                                    .replaceFirst("%", playerList[playerList.size % playerIndex.intValue].name.value)
                            }
                        },
                        modifier = Modifier.align(alignment = Alignment.BottomCenter)
                    )
                }
            }
        }

        if (isAlertDialogOpen.value) {
            CustomAlertDialog(
                isAlertDialogOpen = isAlertDialogOpen,
                title = alertDialogTitle.value,
                text = alertDialogText.value
            )
        }
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

private fun initActionCards(context: Context, playerList: List<Player>): MutableList<Card> {
    val actionCards: MutableList<Card> = mutableListOf()

    var playerIndex = 1
    while (true) {
        val selectedEroZoneMutableList: MutableList<EroZoneMutable> =
            playerList[(playerIndex % playerList.size)].selectedEroZones.toMutableList()
        val eroZoneMutable: EroZoneMutable? = getEroZone(selectedEroZoneMutableList)

        if (eroZoneMutable != null) {
            actionCards.add(
                card(
                    context = context,
                    eroZoneMutable = eroZoneMutable,
                    activePlayer = playerList[((playerIndex - 1) % playerList.size)],
                    passivePlayer = playerList[(playerIndex++ % playerList.size)]
                )
            )
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

    eroZoneMutable = selectedEroZoneMutableList.filter { it.eroZoneType == EroZoneType.HOT }.randomOrNull()
    if (eroZoneMutable != null) {
        if (eroZoneMutable.actionList.isNotEmpty()) {
            return eroZoneMutable
        } else {
            selectedEroZoneMutableList.remove(eroZoneMutable)
            getEroZone(selectedEroZoneMutableList)
        }
    }

    eroZoneMutable = selectedEroZoneMutableList.filter { it.eroZoneType == EroZoneType.HARD }.randomOrNull()
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

private fun card(
    context: Context,
    eroZoneMutable: EroZoneMutable,
    activePlayer: Player,
    passivePlayer: Player
) = createCard(
    cardFrontContent = buildStylizedText(
        simpleText = LocalizationManager.getLocalizedString(
            context = context,
            resourceId = getAction(eroZoneMutable)?.resourceId ?: 0
        )
            .replaceFirst("%", activePlayer.name.value)
            .replaceFirst("%", passivePlayer.name.value),
        stylizedTexts = listOf(activePlayer.name.value, passivePlayer.name.value),
        cardColor = if (passivePlayer.gender.value == Gender.MALE) MaleColor else FemaleColor
    ),
    gender = passivePlayer.gender.value
)

private fun createCard(
    cardFrontContent: AnnotatedString,
    gender: Gender
): Card {
    return Card(
        CardFront(
            content = cardFrontContent,
            color = setBackgroundColor(gender)
        ),
        CardBack(color = setBackgroundColor(gender))
    )
}

private fun getAction(eroZoneMutable: EroZoneMutable): Action? {
    var action: Action? = eroZoneMutable.actionList
        .filter { it.actionType == ActionType.SOFT }
        .randomOrNull()

    if (action != null) {
        eroZoneMutable.actionList.remove(action)
        return action
    }

    action = eroZoneMutable.actionList.filter { it.actionType == ActionType.HOT }.randomOrNull()
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

private fun buildStylizedText(simpleText: String, stylizedTexts: List<String>, cardColor: Color): AnnotatedString {
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
