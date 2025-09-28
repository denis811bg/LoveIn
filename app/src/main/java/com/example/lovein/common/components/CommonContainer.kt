package com.example.lovein.common.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.lovein.common.constants.DescriptionConstants
import com.example.lovein.common.data.Gender
import com.example.lovein.common.data.NavigationScreens
import com.example.lovein.common.models.Partner
import com.example.lovein.createpartnerlist.CreatePartnerListScreen
import com.example.lovein.ui.theme.BackgroundDarkBlueColor
import com.example.lovein.ui.theme.BackgroundDarkPinkColor
import com.example.lovein.ui.theme.BackgroundLightBlueColor
import com.example.lovein.ui.theme.BackgroundLightPinkColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonContainer(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    content: @Composable (contentPadding: PaddingValues) -> Unit
) {
    val isLanguageSelectionScreen = navController.currentBackStackEntry?.destination?.route ==
            NavigationScreens.LANGUAGE_SELECTION_SCREEN.name

    val isInfoScreen = navController.currentBackStackEntry?.destination?.route ==
            NavigationScreens.INFO_SCREEN.name


    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawPath(
                path = Path().apply {
                    moveTo(0f, 0f)
                    lineTo(size.width / 4 * 3, 0f)
                    lineTo(size.width / 4, size.height)
                    lineTo(0f, size.height)
                },
                brush = Brush.linearGradient(
                    colors = listOf(BackgroundLightBlueColor, BackgroundDarkBlueColor),
                    start = Offset(size.width / 4 * 3, 0f),
                    end = Offset(0f, size.height)
                )
            )

            drawPath(
                path = Path().apply {
                    moveTo(size.width / 4 * 3, 0f)
                    lineTo(size.width, 0f)
                    lineTo(size.width, size.height)
                    lineTo(size.width / 4, size.height)
                },
                brush = Brush.linearGradient(
                    colors = listOf(BackgroundLightPinkColor, BackgroundDarkPinkColor),
                    start = Offset(size.width / 4, size.height),
                    end = Offset(size.width, 0f)
                )
            )
        }

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding(),
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(text = "") },
                    navigationIcon = {
                        if (navController.previousBackStackEntry != null) {
                            IconButton(onClick = { navController.navigateUp() }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = DescriptionConstants.ARROW_BACK_ICON,
                                    tint = Color.White
                                )
                            }
                        }
                    },
                    actions = {
                        if (!isLanguageSelectionScreen) {
                            IconButton(
                                onClick = {
                                    navController.navigate(route = NavigationScreens.LANGUAGE_SELECTION_SCREEN.name)
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Language,
                                    contentDescription = DescriptionConstants.LANGUAGE_ICON,
                                    tint = Color.White
                                )
                            }
                        }

                        if (!isInfoScreen) {
                            IconButton(
                                onClick = { navController.navigate(route = NavigationScreens.INFO_SCREEN.name) }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = DescriptionConstants.INFO_ICON,
                                    tint = Color.White
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            },
            containerColor = Color.Transparent,
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { innerPadding ->
            content(innerPadding)
        }
    }
}

@Preview
@Composable
fun CommonContainerPreview() {
    val navController: NavController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val partnerList: MutableList<MutableState<Partner>> = remember {
        mutableStateListOf(
            mutableStateOf(Partner(Gender.MALE)),
            mutableStateOf(Partner(Gender.FEMALE))
        )
    }

    CommonContainer(
        navController = navController,
        snackbarHostState = snackbarHostState
    ) {
        CreatePartnerListScreen(
            navController = navController,
            partnerList = partnerList
        )
    }
}
