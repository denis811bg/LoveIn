package com.example.lovein.common.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lovein.ui.theme.NavigateButtonColor
import com.example.lovein.ui.theme.montserratFontFamily

@Composable
fun CommonNavigationButton(
    text: String,
    icon: ImageVector,
    iconContentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier
) {
    ExtendedFloatingActionButton(
        text = {
            Text(
                text = text,
                fontSize = 32.sp,
                fontFamily = montserratFontFamily
            )
        },
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = iconContentDescription,
                modifier = Modifier.size(48.dp)
            )
        },
        onClick = onClick,
        modifier = modifier,
        shape = FloatingActionButtonDefaults.largeShape,
        containerColor = NavigateButtonColor,
        contentColor = Color.White,
        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
    )
}
