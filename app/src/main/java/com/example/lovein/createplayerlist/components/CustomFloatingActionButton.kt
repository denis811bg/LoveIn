package com.example.lovein.createplayerlist.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun CustomFloatingActionButton(
    onClick: () -> Unit,
    leftShape: Boolean,
    rightShape: Boolean,
    containerColor: Color,
    icon: ImageVector,
    iconContentDescriptor: String
) {
    FloatingActionButton(
        onClick = onClick,
        shape = RoundedCornerShape(
            topStart = if (leftShape) 8.dp else 0.dp,
            topEnd = if (rightShape) 8.dp else 0.dp,
            bottomStart = if (leftShape) 8.dp else 0.dp,
            bottomEnd = if (rightShape) 8.dp else 0.dp
        ),
        containerColor = containerColor,
        contentColor = Color.White
    ) {
        Icon(
            imageVector = icon,
            contentDescription = iconContentDescriptor,
            modifier = Modifier.size(32.dp),
        )
    }
}
