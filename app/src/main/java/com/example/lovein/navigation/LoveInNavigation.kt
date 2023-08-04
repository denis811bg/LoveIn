package com.example.lovein.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import com.example.lovein.common.data.Gender
import com.example.lovein.common.data.NavigationScreens
import com.example.lovein.common.dtos.PlayerDTO
import com.example.lovein.common.models.Player
import com.example.lovein.createplayerlist.CreatePlayerListScreen
import com.example.lovein.erozoneexplorer.EroZoneExplorerScreen
import com.example.lovein.info.InfoScreen
import com.example.lovein.languageselection.LanguageSelectionScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun LoveInNavigation() {
    val navController: NavHostController = rememberAnimatedNavController()
    val players: MutableList<MutableState<Player>> = remember {
        mutableStateListOf(
            mutableStateOf(Player(Gender.MALE))
        )
    }

    AnimatedNavHost(
        navController = navController,
        startDestination = NavigationScreens.CREATE_PLAYER_LIST_SCREEN.name
    ) {
        composable(
            route = NavigationScreens.CREATE_PLAYER_LIST_SCREEN.name,
            enterTransition = {
                when (initialState.destination.route) {
                    NavigationScreens.ERO_ZONE_EXPLORER_SCREEN.name ->
                        slideIntoContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Right,
                            animationSpec = tween(durationMillis = 500)
                        )

                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    NavigationScreens.ERO_ZONE_EXPLORER_SCREEN.name ->
                        slideOutOfContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Left,
                            animationSpec = tween(durationMillis = 500)
                        )

                    else -> null
                }
            },
        ) {
            CreatePlayerListScreen(
                navController = navController,
                players = players
            )
        }

        composable(
            route = NavigationScreens.ERO_ZONE_EXPLORER_SCREEN.name,
            enterTransition = {
                when (initialState.destination.route) {
                    NavigationScreens.CREATE_PLAYER_LIST_SCREEN.name ->
                        slideIntoContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Left,
                            animationSpec = tween(durationMillis = 500)
                        )

                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    NavigationScreens.CREATE_PLAYER_LIST_SCREEN.name ->
                        slideOutOfContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Right,
                            animationSpec = tween(durationMillis = 500)
                        )

                    else -> null
                }
            }
        ) {
            val playerDTOList: List<PlayerDTO>? =
                navController.previousBackStackEntry?.savedStateHandle?.get<List<PlayerDTO>>("playerDTOList")
            EroZoneExplorerScreen(
                navController = navController,
                playerDTOList = playerDTOList ?: emptyList()
            )
        }

        composable(
            route = NavigationScreens.LANGUAGE_SELECTION_SCREEN.name,
            enterTransition = { null },
            exitTransition = { null }
        ) {
            LanguageSelectionScreen(navController = navController)
        }

        composable(
            route = NavigationScreens.INFO_SCREEN.name,
            enterTransition = { null },
            exitTransition = { null }
        ) {
            InfoScreen(navController = navController)
        }
    }
}
