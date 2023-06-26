package com.example.lovein.common.components

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.navigation.NavController
import com.example.lovein.ui.theme.BackgroundDarkBlueColor
import com.example.lovein.ui.theme.BackgroundDarkPinkColor
import com.example.lovein.ui.theme.BackgroundLightBlueColor
import com.example.lovein.ui.theme.BackgroundLightPinkColor

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonContainer(
    navController: NavController,
    content: @Composable () -> Unit
) {
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
            modifier = Modifier.fillMaxSize(),
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(text = "") },
                    navigationIcon = {
                        if (navController.previousBackStackEntry != null) {
                            IconButton(onClick = { navController.navigateUp() }) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "arrow_back_icon",
                                    tint = Color.White
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            },
            containerColor = Color.Transparent
        ) {
            content()
        }
    }
}
