package com.example.lovein.languageselection

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.lovein.common.components.CommonContainer
import com.example.lovein.common.data.Language
import com.example.lovein.common.objects.LocalizationManager
import com.example.lovein.ui.theme.BackgroundDarkBlueColor
import com.example.lovein.ui.theme.BackgroundDarkPinkColor
import com.example.lovein.ui.theme.helveticaFontFamily

@Composable
fun LanguageSelectionScreen(navController: NavController) {
    val context = LocalContext.current

    CompositionLocalProvider(LocalConfiguration provides context.resources.configuration) {
        CommonContainer(navController = navController) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = 16.dp,
                        top = innerPadding.calculateTopPadding(),
                        end = 16.dp,
                        bottom = 16.dp
                    )
            ) {
                items(
                    items = Language.values()
                ) { language ->
                    TextButton(
                        onClick = {
                            LocalizationManager.setCurrentLocale(language.langAbbrev, context)
                            navController.navigateUp()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(BackgroundDarkPinkColor, BackgroundDarkBlueColor),
                                    start = Offset.Zero,
                                    end = Offset.Infinite
                                ),
                                shape = FloatingActionButtonDefaults.largeShape
                            ),
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = Color.White
                        ),
                        elevation = ButtonDefaults.elevatedButtonElevation(
                            defaultElevation = 0.dp,
                            pressedElevation = 2.dp
                        )
                    ) {
                        Text(
                            text = language.label,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = helveticaFontFamily
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun LanguageSelectionScreenPreview() {
    val navController: NavController = rememberNavController()

    LanguageSelectionScreen(navController = navController)
}