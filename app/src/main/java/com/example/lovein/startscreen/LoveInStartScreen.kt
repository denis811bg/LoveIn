package com.example.lovein.startscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.lovein.common.components.CommonContainer
import com.example.lovein.common.components.CommonNavigationButton
import com.example.lovein.common.data.NavigationScreens

@Composable
fun LoveInStartScreen(navController: NavController) {
    CommonContainer(navController = navController) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.aligned(alignment = Alignment.CenterVertically)
        ) {
            CommonNavigationButton(
                text = "Start",
                icon = Icons.Default.PlayCircle,
                iconContentDescription = "play_circle_icon",
                onClick = { navController.navigate(route = NavigationScreens.CREATE_PLAYER_LIST_SCREEN.name) },
                modifier = Modifier
            )
        }
    }
}
