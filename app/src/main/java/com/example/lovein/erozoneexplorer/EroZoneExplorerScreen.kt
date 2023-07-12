package com.example.lovein.erozoneexplorer

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.lovein.common.components.CommonContainer
import com.example.lovein.common.components.CommonNavigationButton
import com.example.lovein.common.data.EroZone
import com.example.lovein.common.data.Gender
import com.example.lovein.common.dtos.PlayerDTO
import com.example.lovein.erozoneexplorer.components.Stack
import com.example.lovein.erozoneexplorer.models.Card
import com.example.lovein.erozoneexplorer.models.CardBack
import com.example.lovein.erozoneexplorer.models.CardFront
import com.example.lovein.ui.theme.FemaleColor
import com.example.lovein.ui.theme.MaleColor

private const val ACTION = "Action"
private const val ERO_ZONE = "Ero zone"

@Composable
fun EroZoneExplorerScreen(
    navController: NavController,
    playerDTOList: List<PlayerDTO>
) {
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
            createCard(
                cardFrontContent = passivePlayerRandomEroZone.value.actionList.random().label,
                cardBackContent = ACTION,
                gender = passivePlayer.value.gender
            ),
            createCard(
                cardFrontContent = nextPassivePlayerRandomEroZone.value.actionList.random().label,
                cardBackContent = ACTION,
                gender = nextPassivePlayer.value.gender
            )
        )
    }
    val eroZonesCards: MutableList<Card> = remember {
        mutableListOf(
            createCard(
                cardFrontContent = passivePlayerRandomEroZone.value.label,
                cardBackContent = ERO_ZONE,
                gender = passivePlayer.value.gender
            ),
            createCard(
                cardFrontContent = nextPassivePlayerRandomEroZone.value.label,
                cardBackContent = ERO_ZONE,
                gender = nextPassivePlayer.value.gender
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
            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Spacer(modifier = Modifier.height(20.dp))

                    AnimatedContent(
                        targetState = activePlayer.value.name,
                        transitionSpec = {
                            fadeIn(tween(3000)) togetherWith fadeOut(tween(3000))
                        }
                    ) { targetActivePlayerName ->
                        Text(
                            text = targetActivePlayerName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 32.sp,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Stack(
                        cards = actionCards,
                        position = activePlayerIndex.intValue,
                        modifier = Modifier.aspectRatio(2f),
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    AnimatedContent(
                        targetState = passivePlayer.value.name,
                        transitionSpec = {
                            fadeIn(tween(3000)) togetherWith fadeOut(tween(3000))
                        }
                    ) { targetPassivePlayerName ->
                        Text(
                            text = "${targetPassivePlayerName}'s",
                            fontWeight = FontWeight.Bold,
                            fontSize = 32.sp,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Stack(
                        cards = eroZonesCards,
                        position = activePlayerIndex.intValue,
                        modifier = Modifier.aspectRatio(2f)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            CommonNavigationButton(
                text = "Play",
                icon = Icons.Default.PlayCircle,
                iconContentDescription = "play_circle_icon",
                onClick = {
                    activePlayer.value = nextActivePlayer.value
                    passivePlayer.value = nextPassivePlayer.value
                    passivePlayerRandomEroZone.value = nextPassivePlayerRandomEroZone.value

                    nextActivePlayer.value = passivePlayer.value
                    nextPassivePlayer.value = playerDTOList[(++activePlayerIndex.intValue + 2) % playerDTOList.size]
                    nextPassivePlayerRandomEroZone.value = nextPassivePlayer.value.selectedEroZoneList.random()

                    actionCards.add(
                        createCard(
                            cardFrontContent = nextPassivePlayerRandomEroZone.value.actionList.random().label,
                            cardBackContent = ACTION,
                            gender = nextPassivePlayer.value.gender
                        )
                    )
                    eroZonesCards.add(
                        createCard(
                            cardFrontContent = nextPassivePlayerRandomEroZone.value.label,
                            cardBackContent = ERO_ZONE,
                            gender = nextPassivePlayer.value.gender
                        )
                    )
                },
                modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
        }
    }
}

private fun createCard(
    cardFrontContent: String,
    cardBackContent: String,
    gender: Gender
): Card {
    return Card(
        CardFront(
            content = cardFrontContent,
            color = setBackgroundColor(gender)
        ),
        CardBack(
            content = cardBackContent,
            color = setBackgroundColor(gender)
        )
    )
}

private fun setBackgroundColor(gender: Gender): Color {
    return if (gender == Gender.MALE)
        MaleColor
    else
        FemaleColor
}
