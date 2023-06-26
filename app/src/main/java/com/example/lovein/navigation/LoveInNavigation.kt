package com.example.lovein.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.lovein.common.data.NavigationScreens
import com.example.lovein.createplayerlist.CreatePlayerListScreen
import com.example.lovein.erozoneexplorer.EroZoneExplorerScreen
import com.example.lovein.startscreen.LoveInStartScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

private const val PLAYER_DTO_LIST: String = "playerDTOList"

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun LoveInNavigation() {
    val navController: NavHostController = rememberAnimatedNavController()

    AnimatedNavHost(
        navController = navController,
        startDestination = NavigationScreens.START_SCREEN.name
    ) {
        composable(
            route = NavigationScreens.START_SCREEN.name,
            enterTransition = {
                when (initialState.destination.route) {
                    NavigationScreens.CREATE_PLAYER_LIST_SCREEN.name ->
                        slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(500))
                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    NavigationScreens.CREATE_PLAYER_LIST_SCREEN.name ->
                        slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(500))
                    else -> null
                }
            },
        ) {
            LoveInStartScreen(navController = navController)
        }

        composable(
            route = NavigationScreens.CREATE_PLAYER_LIST_SCREEN.name,
            enterTransition = {
                when (initialState.destination.route) {
                    NavigationScreens.START_SCREEN.name ->
                        slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(500))
                    NavigationScreens.ERO_ZONE_EXPLORER_SCREEN.name ->
                        slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(500))
                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    NavigationScreens.START_SCREEN.name ->
                        slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(500))
                    NavigationScreens.ERO_ZONE_EXPLORER_SCREEN.name ->
                        slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(500))
                    else -> null
                }
            }
        ) {
            CreatePlayerListScreen(navController = navController)
        }

        composable(
            route = NavigationScreens.ERO_ZONE_EXPLORER_SCREEN.name + "/{" + PLAYER_DTO_LIST + "}",
            enterTransition = {
                when (initialState.destination.route) {
                    NavigationScreens.CREATE_PLAYER_LIST_SCREEN.name ->
                        slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(500))
                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    NavigationScreens.CREATE_PLAYER_LIST_SCREEN.name ->
                        slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(500))
                    else -> null
                }
            },
            arguments = listOf(navArgument(PLAYER_DTO_LIST) { type = NavType.StringType })
        ) { backStackEntry ->
            EroZoneExplorerScreen(
                playerDTOList = Json.decodeFromString(
                    backStackEntry.arguments?.getString(PLAYER_DTO_LIST).toString()
                ),
                navController = navController
            )
        }
    }
}
