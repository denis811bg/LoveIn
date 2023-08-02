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
import com.example.lovein.common.data.EroZone
import com.example.lovein.common.data.Gender
import com.example.lovein.common.dtos.PlayerDTO
import com.example.lovein.common.objects.LocalizationManager
import com.example.lovein.erozoneexplorer.components.Stack
import com.example.lovein.erozoneexplorer.models.Card
import com.example.lovein.erozoneexplorer.models.CardBack
import com.example.lovein.erozoneexplorer.models.CardFront
import com.example.lovein.ui.theme.BackgroundLightBlueColor
import com.example.lovein.ui.theme.BackgroundLightPinkColor
import com.example.lovein.ui.theme.FemaleColor
import com.example.lovein.ui.theme.MaleColor

@Composable
fun EroZoneExplorerScreen(
    navController: NavController,
    playerDTOList: List<PlayerDTO>
) {
    val context: Context = LocalContext.current

    val activePlayerIndex: MutableIntState = remember { mutableStateOf(0) }
    val activePlayer: MutableState<PlayerDTO> =
        remember { mutableStateOf(playerDTOList[activePlayerIndex.intValue]) }
    val passivePlayer: MutableState<PlayerDTO> =
        remember { mutableStateOf(playerDTOList[(activePlayerIndex.intValue + 1) % playerDTOList.size]) }
    val passivePlayerRandomEroZone: MutableState<EroZone> =
        remember { mutableStateOf(passivePlayer.value.selectedEroZoneList.random()) }
    val nextActivePlayer: MutableState<PlayerDTO> = passivePlayer
    val nextPassivePlayer: MutableState<PlayerDTO> =
        remember { mutableStateOf(playerDTOList[(activePlayerIndex.intValue + 2) % playerDTOList.size]) }
    val nextPassivePlayerRandomEroZone: MutableState<EroZone> =
        remember { mutableStateOf(nextPassivePlayer.value.selectedEroZoneList.random()) }
    val actionCards: MutableList<Card> = remember {
        mutableListOf(
            card(
                context = context,
                randomEroZone = passivePlayerRandomEroZone,
                activePlayer = activePlayer,
                passivePlayer = passivePlayer
            ),
            card(
                context = context,
                randomEroZone = nextPassivePlayerRandomEroZone,
                activePlayer = nextActivePlayer,
                passivePlayer = nextPassivePlayer
            )
        )
    }

    CommonContainer(navController = navController) { innerPadding ->
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
                        position = activePlayerIndex.intValue
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
                            activePlayer.value = nextActivePlayer.value
                            passivePlayer.value = nextPassivePlayer.value
                            passivePlayerRandomEroZone.value = nextPassivePlayerRandomEroZone.value

                            nextActivePlayer.value = passivePlayer.value
                            nextPassivePlayer.value =
                                playerDTOList[(++activePlayerIndex.intValue + 2) % playerDTOList.size]
                            nextPassivePlayerRandomEroZone.value = nextPassivePlayer.value.selectedEroZoneList.random()

                            actionCards.add(
                                card(
                                    context = context,
                                    randomEroZone = nextPassivePlayerRandomEroZone,
                                    activePlayer = nextActivePlayer,
                                    passivePlayer = nextPassivePlayer
                                )
                            )
                        },
                        modifier = Modifier.align(alignment = Alignment.BottomCenter)
                    )
                }
            }
        }
    }
}

private fun card(
    context: Context,
    randomEroZone: MutableState<EroZone>,
    activePlayer: MutableState<PlayerDTO>,
    passivePlayer: MutableState<PlayerDTO>
) = createCard(
    cardFrontContent = buildStylizedText(
        simpleText = LocalizationManager.getLocalizedString(
            context = context,
            resourceId = randomEroZone.value.actionList.random().resourceId
        )
            .replaceFirst("%", activePlayer.value.name)
            .replaceFirst("%", passivePlayer.value.name),
        stylizedTexts = listOf(activePlayer.value.name, passivePlayer.value.name),
        cardColor = if (passivePlayer.value.gender == Gender.MALE) MaleColor else FemaleColor
    ),
    gender = passivePlayer.value.gender
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

private fun setBackgroundColor(gender: Gender): Color {
    return if (gender == Gender.MALE)
        MaleColor
    else
        FemaleColor
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
                    EroZone.SCALP_AND_HAIR
                )
            ),
            PlayerDTO(
                "Alena",
                Gender.FEMALE,
                listOf(
                    EroZone.SCALP_AND_HAIR
                )
            ),
            PlayerDTO(
                "Sofi",
                Gender.FEMALE,
                listOf(
                    EroZone.SCALP_AND_HAIR
                )
            )
        )

    EroZoneExplorerScreen(
        navController = navController,
        playerDTOList = playerDTOList
    )
}

fun buildStylizedText(simpleText: String, stylizedTexts: List<String>, cardColor: Color): AnnotatedString {
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
