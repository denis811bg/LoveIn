package com.example.lovein.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lovein.common.data.Gender
import com.example.lovein.common.data.NavigationScreens
import com.example.lovein.common.dtos.PlayerDTO
import com.example.lovein.common.models.Player
import com.example.lovein.createplayerlist.CreatePlayerListScreen
import com.example.lovein.erozoneexplorer.EroZoneExplorerScreen
import com.example.lovein.info.InfoScreen
import com.example.lovein.languageselection.LanguageSelectionScreen

@Composable
fun LoveInNavigation() {
    val navController: NavHostController = rememberNavController()
    val playerList: MutableList<MutableState<Player>> = remember {
        mutableStateListOf(
            mutableStateOf(Player(Gender.MALE)),
            mutableStateOf(Player(Gender.FEMALE))
        )
    }

    NavHost(
        navController = navController,
        startDestination = NavigationScreens.CREATE_PLAYER_LIST_SCREEN.name
    ) {
        composable(
            route = NavigationScreens.CREATE_PLAYER_LIST_SCREEN.name,
            enterTransition = { null },
            exitTransition = { null }
        ) {
            CreatePlayerListScreen(
                navController = navController,
                playerList = playerList
            )
        }

        composable(
            route = NavigationScreens.ERO_ZONE_EXPLORER_SCREEN.name,
            enterTransition = { null },
            exitTransition = { null }
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
