package com.example.lovein.info

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.lovein.R
import com.example.lovein.common.components.CommonContainer
import com.example.lovein.ui.theme.BackgroundDarkBlueColor
import com.example.lovein.ui.theme.BackgroundDarkPinkColor
import com.example.lovein.ui.theme.helveticaFontFamily

@Composable
fun InfoScreen(navController: NavController) {
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }

    CommonContainer(
        navController = navController,
        snackbarHostState = snackbarHostState
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 16.dp,
                    top = innerPadding.calculateTopPadding(),
                    end = 16.dp,
                    bottom = 16.dp
                )
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(BackgroundDarkPinkColor, BackgroundDarkBlueColor),
                        start = Offset.Zero,
                        end = Offset.Infinite
                    ),
                    shape = FloatingActionButtonDefaults.smallShape
                )
        ) {
            Text(
                text = stringResource(R.string.about_app_description),
                modifier = Modifier
                    .padding(8.dp)
                    .verticalScroll(scrollState),
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = helveticaFontFamily,
                textAlign = TextAlign.Justify
            )
        }
    }
}

@Preview
@Composable
fun InfoScreenPreview() {
    val navController = rememberNavController()

    InfoScreen(navController = navController)
}
