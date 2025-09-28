package com.example.lovein.common.components

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.lovein.R
import com.example.lovein.common.data.NavigationScreens
import com.example.lovein.common.objects.LocalizationManager
import com.example.lovein.ui.theme.AlertDialogConfirmButtonColor
import com.example.lovein.ui.theme.helveticaFontFamily

@Composable
fun CustomAlertDialog(
    isAlertDialogOpen: MutableState<Boolean>,
    title: String,
    text: String,
    navController: NavController,
    onDownloadClick: (() -> Unit)? = null
) {
    val context: Context = LocalContext.current

    AlertDialog(
        onDismissRequest = { isAlertDialogOpen.value = false },
        confirmButton = {
            if (onDownloadClick != null) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextButton(onClick = onDownloadClick) {
                        Text(
                            text = LocalizationManager.getLocalizedString(
                                context,
                                R.string.download_body_map_and_rating
                            ),
                            color = AlertDialogConfirmButtonColor,
                            fontSize = 16.sp,
                            fontFamily = helveticaFontFamily
                        )
                    }
                    OkButton(isAlertDialogOpen, navController, context)
                }
            } else {
                OkButton(isAlertDialogOpen, navController, context)
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

@Composable
private fun OkButton(
    isAlertDialogOpen: MutableState<Boolean>,
    navController: NavController,
    context: Context
) {
    TextButton(
        onClick = {
            isAlertDialogOpen.value = false

            if (navController.currentDestination?.route == NavigationScreens.ERO_ZONE_EXPLORER_SCREEN.name) {
                navController.navigateUp()
            }
        }
    ) {
        Text(
            text = LocalizationManager.getLocalizedString(context, R.string.ok_button),
            color = AlertDialogConfirmButtonColor,
            fontSize = 16.sp,
            fontFamily = helveticaFontFamily,
        )
    }
}
