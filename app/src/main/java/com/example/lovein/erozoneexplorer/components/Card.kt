package com.example.lovein.erozoneexplorer.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lovein.R
import com.example.lovein.common.models.FeedbackType
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
                is CardFront -> CardFrontContent(
                    cardFace = cardFace,
                    color = cardFace.color
                )

                is CardBack -> CardBackContent(color = cardFace.color)
            }
        }
    }
}

@Composable
private fun CardFrontContent(
    cardFace: CardFront,
    color: Color,
) {
    val scrollState = rememberScrollState()
    val feedback = cardFace.actionWithFeedback.feedback

    CardContainer(color = color) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(scrollState),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = cardFace.content,
                    modifier = Modifier.padding(8.dp),
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = helveticaFontFamily,
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                IconButton(
                    onClick = {
                        cardFace.actionWithFeedback.feedback.value = FeedbackType.LIKE
                    },
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.ThumbUp,
                        contentDescription = "Like",
                        modifier = Modifier.size(48.dp),
                        tint = if (feedback.value == FeedbackType.LIKE) Color.White else Color.Gray
                    )
                }
                IconButton(
                    onClick = {
                        cardFace.actionWithFeedback.feedback.value = FeedbackType.DISLIKE
                    },
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.ThumbDown,
                        contentDescription = "Dislike",
                        modifier = Modifier.size(48.dp),
                        tint = if (feedback.value == FeedbackType.DISLIKE) Color.White else Color.Gray
                    )
                }
            }
        }
    }
}


@Composable
private fun CardBackContent(color: Color) {
    CardContainer(color = color) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = color),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.bg_card_1),
                contentDescription = "bg_card",
                contentScale = ContentScale.Inside,
                colorFilter = ColorFilter.tint(color = Color.White)
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

@Preview
@Composable
fun CardContainerPreview() {
    val color: Color = FemaleColor

    CardContainer(color = color) {
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
                        elevation = 16.dp,
                        shape = RoundedCornerShape(16.dp)
                    ),
                shape = RoundedCornerShape(16.dp),
                color = color,
                border = BorderStroke(1.dp, Color.Transparent)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = color),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.bg_card_0),
                        contentDescription = "bg_card",
                        contentScale = ContentScale.Inside
                    )
                }
            }
        }
    }
}
