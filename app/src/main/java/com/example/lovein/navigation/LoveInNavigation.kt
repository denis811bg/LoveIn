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
import com.example.lovein.common.constants.NavigationConstants
import com.example.lovein.common.data.Gender
import com.example.lovein.common.data.NavigationScreens
import com.example.lovein.common.dtos.PartnerDTO
import com.example.lovein.common.models.Partner
import com.example.lovein.createpartnerlist.CreatePartnerListScreen
import com.example.lovein.erozoneexplorer.EroZoneExplorerScreen
import com.example.lovein.info.InfoScreen
import com.example.lovein.languageselection.LanguageSelectionScreen

@Composable
fun LoveInNavigation() {
    val navController: NavHostController = rememberNavController()
    val partnerList: MutableList<MutableState<Partner>> = remember {
        mutableStateListOf(
            mutableStateOf(Partner(Gender.MALE)),
            mutableStateOf(Partner(Gender.FEMALE))
        )
    }

    NavHost(
        navController = navController,
        startDestination = NavigationScreens.CREATE_PARTNER_LIST_SCREEN.name
    ) {
        composable(
            route = NavigationScreens.CREATE_PARTNER_LIST_SCREEN.name,
            enterTransition = { null },
            exitTransition = { null }
        ) {
            CreatePartnerListScreen(
                navController = navController,
                partnerList = partnerList
            )
        }

        composable(
            route = NavigationScreens.ERO_ZONE_EXPLORER_SCREEN.name,
            enterTransition = { null },
            exitTransition = { null }
        ) {
            val partnerDTOLists: List<PartnerDTO>? =
                navController.previousBackStackEntry?.savedStateHandle?.get<List<PartnerDTO>>(NavigationConstants.PARTNER_DTO_LIST_KEY)
            EroZoneExplorerScreen(
                navController = navController,
                partnerDTOList = partnerDTOLists ?: emptyList()
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
