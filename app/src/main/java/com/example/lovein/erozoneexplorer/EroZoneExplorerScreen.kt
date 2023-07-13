package com.example.lovein.erozoneexplorer

import android.content.Context
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            createCard(
                cardFrontContent = passivePlayerRandomEroZone.value.actionList.random().label,
                cardBackContent = LocalizationManager.getLocalizedString(context, R.string.action),
                gender = passivePlayer.value.gender
            ),
            createCard(
                cardFrontContent = nextPassivePlayerRandomEroZone.value.actionList.random().label,
                cardBackContent = LocalizationManager.getLocalizedString(context, R.string.action),
                gender = nextPassivePlayer.value.gender
            )
        )
    }
    val eroZonesCards: MutableList<Card> = remember {
        mutableListOf(
            createCard(
                cardFrontContent = LocalizationManager.getLocalizedString(
                    context,
                    passivePlayerRandomEroZone.value.resourceId
                ),
                cardBackContent = LocalizationManager.getLocalizedString(context, R.string.ero_zone),
                gender = passivePlayer.value.gender
            ),
            createCard(
                cardFrontContent = LocalizationManager.getLocalizedString(
                    context,
                    nextPassivePlayerRandomEroZone.value.resourceId
                ),
                cardBackContent = LocalizationManager.getLocalizedString(context, R.string.ero_zone),
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

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
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

            Spacer(modifier = Modifier.weight(1f))

            CommonNavigationButton(
                text = LocalizationManager.getLocalizedString(context, R.string.play_button),
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
                            cardBackContent = LocalizationManager.getLocalizedString(context, R.string.action),
                            gender = nextPassivePlayer.value.gender
                        )
                    )
                    eroZonesCards.add(
                        createCard(
                            cardFrontContent = LocalizationManager.getLocalizedString(
                                context,
                                nextPassivePlayerRandomEroZone.value.resourceId
                            ),
                            cardBackContent = LocalizationManager.getLocalizedString(context, R.string.ero_zone),
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
                    EroZone.GLANS,
                    EroZone.FRENULUM,
                    EroZone.FORESKIN,
                    EroZone.SCROTUM_AND_TESTICLES,
                    EroZone.PERINEUM,
                    EroZone.PROSTATE
                )
            ),
            PlayerDTO(
                "Alena",
                Gender.FEMALE,
                listOf(
                    EroZone.AREOLA_AND_NIPPLES,
                    EroZone.PUBIC_MOUND,
                    EroZone.CLITORIS,
                    EroZone.A_SPOT,
                    EroZone.G_SPOT,
                    EroZone.CERVIX
                )
            )
        )

    EroZoneExplorerScreen(
        navController = navController,
        playerDTOList = playerDTOList
    )
}
