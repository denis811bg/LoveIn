package com.example.lovein.createplayerlist.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.lovein.common.data.NavigationScreens
import com.example.lovein.ui.theme.AlertDialogConfirmButtonColor
import com.example.lovein.ui.theme.helveticaFontFamily

@Composable
fun CustomAlertDialog(
    isAlertDialogOpen: MutableState<Boolean>,
    title: String,
    text: String,
    navController: NavController
) {
    AlertDialog(
        onDismissRequest = { isAlertDialogOpen.value = false },
        confirmButton = {
            TextButton(
                onClick = {
                    isAlertDialogOpen.value = false

                    if (navController.currentDestination?.route == NavigationScreens.ERO_ZONE_EXPLORER_SCREEN.name) {
                        navController.navigateUp()
                    }
                }
            ) {
                Text(
                    text = "OK",
                    color = AlertDialogConfirmButtonColor,
                    fontSize = 16.sp,
                    fontFamily = helveticaFontFamily,
                )
            }
        },
        title = {
            Text(
                text = title,
                fontFamily = helveticaFontFamily,
                textAlign = TextAlign.Start
            )
        },
        text = {
            Text(
                text = text,
                fontSize = 18.sp,
                fontFamily = helveticaFontFamily,
                textAlign = TextAlign.Start
            )
        },
        shape = RoundedCornerShape(8.dp),
        containerColor = Color.White
    )
}
