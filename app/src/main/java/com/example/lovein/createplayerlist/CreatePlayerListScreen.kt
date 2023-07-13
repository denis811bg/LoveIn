package com.example.lovein.createplayerlist

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.lovein.R
import com.example.lovein.common.components.CommonContainer
import com.example.lovein.common.components.CommonNavigationButton
import com.example.lovein.common.data.Gender
import com.example.lovein.common.data.NavigationScreens
import com.example.lovein.common.dtos.PlayerDTO
import com.example.lovein.common.models.NoRippleTheme
import com.example.lovein.common.models.Player
import com.example.lovein.common.objects.LocalizationManager
import com.example.lovein.createplayerlist.components.CustomAlertDialog
import com.example.lovein.createplayerlist.components.EroZoneListCard
import com.example.lovein.createplayerlist.components.PlayerInputRow
import com.example.lovein.ui.theme.helveticaFontFamily
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun CreatePlayerListScreen(
    navController: NavController,
    players: MutableList<MutableState<Player>>
) {
    val context: Context = LocalContext.current

    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val listState: LazyListState = rememberLazyListState()

    val isAlertDialogOpen: MutableState<Boolean> = remember { mutableStateOf(false) }
    val alertDialogTitle: MutableState<String> = remember { mutableStateOf("") }
    val alertDialogText: MutableState<String> = remember { mutableStateOf("") }

    CommonContainer(navController = navController) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 16.dp,
                    top = innerPadding.calculateTopPadding(),
                    end = 16.dp,
                    bottom = 16.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(size = 8.dp)),
                state = listState,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                itemsIndexed(items = players) { index, player ->
                    val isExpanded: MutableState<Boolean> = remember { mutableStateOf(false) }
                    val alpha = animateFloatAsState(
                        targetValue = if (isExpanded.value) 1f else 0f,
                        animationSpec = tween(durationMillis = 1000)
                    )
                    val rotateX = animateFloatAsState(
                        targetValue = if (isExpanded.value) 0f else -90f,
                        animationSpec = tween(durationMillis = 1000)
                    )

                    PlayerInputRow(
                        playerList = players,
                        index = index,
                        isExpanded = isExpanded
                    )

                    AnimatedVisibility(visible = isExpanded.value) {
                        EroZoneListCard(
                            player = player,
                            rotateX = rotateX,
                            alpha = alpha
                        )
                    }
                }

                item {
                    CompositionLocalProvider(LocalRippleTheme provides NoRippleTheme()) {
                        ExtendedFloatingActionButton(
                            text = {
                                Text(
                                    text = LocalizationManager.getLocalizedString(context, R.string.add_next_player),
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontStyle = FontStyle.Italic,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = helveticaFontFamily
                                )
                            },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.GroupAdd,
                                    contentDescription = "group_add_icon",
                                    tint = Color.White
                                )
                            },
                            onClick = {
                                players.add(mutableStateOf(Player(Gender.MALE)))

                                coroutineScope.launch {
                                    listState.animateScrollToItem(players.size - 1)
                                }
                            },
                            modifier = Modifier.align(alignment = Alignment.Start),
                            containerColor = Color.Transparent,
                            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                        )
                    }
                }
            }

            CommonNavigationButton(
                text = LocalizationManager.getLocalizedString(context, R.string.play_button),
                icon = Icons.Default.PlayCircle,
                iconContentDescription = "play_circle_icon",
                onClick = {
                    if (players.size < 2) {
                        isAlertDialogOpen.value = true
                        alertDialogTitle.value = "Not enough players"
                        alertDialogText.value = "You need at least 2 players to launch the game."
                    } else if (players.any { player -> player.value.name.value == "" }) {
                        isAlertDialogOpen.value = true
                        alertDialogTitle.value = "Add player names"
                        alertDialogText.value = "You need to add player names to launch the game."
                    } else if (players.any { player -> player.value.selectedEroZones.isEmpty() }) {
                        isAlertDialogOpen.value = true
                        alertDialogTitle.value = "Add player ero zones"
                        alertDialogText.value = "You need to add player erogenous zones to launch the game."
                    } else {
                        navController.currentBackStackEntry?.savedStateHandle?.set(
                            key = "playerDTOList",
                            value = convertPlayersToPlayerDTOList(players)
                        )

                        navController.navigate(route = NavigationScreens.ERO_ZONE_EXPLORER_SCREEN.name)
                    }
                },
                modifier = Modifier
                    .padding(top = 16.dp)
                    .imePadding()
            )

            if (isAlertDialogOpen.value) {
                CustomAlertDialog(
                    isAlertDialogOpen = isAlertDialogOpen,
                    title = alertDialogTitle.value,
                    text = alertDialogText.value
                )
            }
        }
    }
}

private fun convertPlayersToPlayerDTOList(players: MutableList<MutableState<Player>>): List<PlayerDTO> {
    return players
        .map { player ->
            PlayerDTO(
                name = player.value.name.value,
                gender = player.value.gender.value,
                selectedEroZoneList = player.value.selectedEroZones.toList()
            )
        }
        .toList()
}
