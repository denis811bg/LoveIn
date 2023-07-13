package com.example.lovein.erozoneexplorer.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lovein.common.dtos.PlayerDTO

@Composable
fun PlayerName(player: MutableState<PlayerDTO>) {
    AnimatedContent(
        targetState = player.value.name,
        transitionSpec = {
            fadeIn(tween(3000)) togetherWith fadeOut(tween(3000))
        }
    ) { targetActivePlayerName ->
        Text(
            text = targetActivePlayerName,
            modifier = Modifier.padding(vertical = 8.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}
