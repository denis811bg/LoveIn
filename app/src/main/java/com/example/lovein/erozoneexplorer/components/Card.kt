package com.example.lovein.erozoneexplorer.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lovein.erozoneexplorer.models.CardBack
import com.example.lovein.erozoneexplorer.models.CardFace
import com.example.lovein.erozoneexplorer.models.CardFront
import com.example.lovein.ui.theme.FemaleColor
import com.example.lovein.ui.theme.MaleColor
import com.example.lovein.ui.theme.helveticaFontFamily

@Composable
fun CardFaceDisplay(
    cardFace: CardFace?,
    modifier: Modifier = Modifier
) {
    if (cardFace != null) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            when (cardFace) {
                is CardFront -> CardFrontContent(cardFace = cardFace, color = cardFace.color)
                is CardBack -> CardBackContent(cardFace = cardFace, color = cardFace.color)
            }
        }
    }
}

@Composable
private fun CardFrontContent(
    cardFace: CardFront,
    color: Color
) {
    CardContainer(color = color) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = color),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = cardFace.content,
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = helveticaFontFamily,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun CardBackContent(
    cardFace: CardBack,
    color: Color
) {
    CardContainer(color = color) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = color),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = cardFace.content,
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = helveticaFontFamily,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun CardContainer(
    color: Color,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = color)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .shadow(
                    elevation = if (color == MaleColor || color == FemaleColor) 16.dp else 0.dp,
                    shape = RoundedCornerShape(16.dp)
                ),
            shape = RoundedCornerShape(16.dp),
            color = color,
            border = BorderStroke(1.dp, Color.Transparent)
        ) {
            content()
        }
    }
}
