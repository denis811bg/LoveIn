package com.example.lovein.createplayerlist.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun CustomFloatingActionButton(
    onClick: () -> Unit,
    leftShape: Boolean,
    rightShape: Boolean,
    containerColor: MutableState<Color>,
    icon: MutableState<ImageVector>,
    iconContentDescriptor: String
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = Modifier.padding(top = 8.dp),
        shape = RoundedCornerShape(
            topStart = if (leftShape) 8.dp else 0.dp,
            topEnd = if (rightShape) 8.dp else 0.dp,
            bottomStart = if (leftShape) 8.dp else 0.dp,
            bottomEnd = if (rightShape) 8.dp else 0.dp
        ),
        containerColor = containerColor.value,
        contentColor = Color.White
    ) {
        Icon(
            imageVector = icon.value,
            contentDescription = iconContentDescriptor,
            modifier = Modifier.size(32.dp),
        )
    }
}
