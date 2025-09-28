package com.example.lovein.createplayerlist

import android.content.Context
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.lovein.common.components.CustomAlertDialog
import com.example.lovein.common.constants.CommonConstants
import com.example.lovein.common.data.NavigationScreens
import com.example.lovein.common.models.Player
import com.example.lovein.common.objects.LocalizationManager
import com.example.lovein.createplayerlist.components.PlayerCard
import com.example.lovein.createplayerlist.validation.validatePlayers
import com.example.lovein.ui.theme.helveticaFontFamily
import com.example.lovein.utils.addRandomPlayer
import com.example.lovein.utils.cleanupActionFeedback
import com.example.lovein.utils.convertPlayerListToPlayerDTOList
import com.example.lovein.utils.showAlertDialog
import kotlinx.coroutines.CoroutineScope

@Composable
fun CreatePlayerListScreen(
    navController: NavController,
    playerList: MutableList<MutableState<Player>>
) {
    val context: Context = LocalContext.current

    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarFlow = navController.currentBackStackEntry!!
        .savedStateHandle
        .getStateFlow(CommonConstants.SNACKBAR_MESSAGE, "")
    val snackbarMessage by snackbarFlow.collectAsState()

    LaunchedEffect(Unit) {
        cleanupActionFeedback(playerList)
        if (snackbarMessage.isNotBlank()) {
            snackbarHostState.showSnackbar(snackbarMessage)
            navController.currentBackStackEntry
                ?.savedStateHandle
                ?.set(CommonConstants.SNACKBAR_MESSAGE, "")
        }
    }

    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val listState: LazyListState = rememberLazyListState()

    val isAlertDialogOpen: MutableState<Boolean> = remember { mutableStateOf(false) }
    val alertDialogTitle: MutableState<String> = remember { mutableStateOf("") }
    val alertDialogText: MutableState<String> = remember { mutableStateOf("") }

    CommonContainer(
        navController = navController,
        snackbarHostState = snackbarHostState
    ) { innerPadding ->
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
                itemsIndexed(items = playerList) { index, player ->
                    PlayerCard(player = player, index = index, playerList = playerList)
                }

                item {
                    ExtendedFloatingActionButton(
                        modifier = Modifier
                            .align(alignment = Alignment.Start)
                            .indication(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ),
                        text = {
                            Text(
                                text = LocalizationManager.getLocalizedString(
                                    context = context,
                                    resourceId = R.string.add_next_player
                                ),
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
                        onClick = { addRandomPlayer(playerList, coroutineScope, listState) },
                        containerColor = Color.Transparent,
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                    )
                }
            }

            CommonNavigationButton(
                text = LocalizationManager.getLocalizedString(
                    context = context,
                    resourceId = R.string.play_button
                ),
                icon = Icons.Default.PlayCircle,
                iconContentDescription = "play_circle_icon",
                onClick = {
                    val result = validatePlayers(playerList)

                    if (!result.isValid) {
                        showAlertDialog(
                            context,
                            result,
                            isAlertDialogOpen,
                            alertDialogTitle,
                            alertDialogText
                        )
                    } else {
                        navController.currentBackStackEntry?.savedStateHandle?.set(
                            key = "playerDTOList",
                            value = convertPlayerListToPlayerDTOList(playerList)
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
                    text = alertDialogText.value,
                    navController = navController
                )
            }
        }
    }
}
