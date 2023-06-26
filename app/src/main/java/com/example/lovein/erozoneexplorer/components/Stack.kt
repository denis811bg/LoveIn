package com.example.lovein.erozoneexplorer.components

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.lovein.erozoneexplorer.models.Card
import com.example.lovein.erozoneexplorer.models.StackViewModel

@Composable
fun Stack(
    cards: List<Card>,
    position: Int,
    modifier: Modifier = Modifier
) {
    val viewModel = hiltViewModel<StackViewModel>()
    viewModel.setCards(cards)
    viewModel.setPosition(position)

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        StackLayout(
            flipCard = viewModel.flipCard,
            leftStack = { modifier ->
                CardFaceDisplay(cardFace = viewModel.leftStackTop?.back, modifier)
            },
            rightStack = { modifier ->
                CardFaceDisplay(cardFace = viewModel.rightStackTop?.front, modifier)
            },
            transitionTrigger = position,
            modifier = modifier
        )
    }
}

@Composable
private fun StackLayout(
    flipCard: Card?,
    leftStack: @Composable (modifier: Modifier) -> Unit,
    rightStack: @Composable (modifier: Modifier) -> Unit,
    transitionTrigger: Int,
    modifier: Modifier = Modifier
) {
    var offset by remember(transitionTrigger) { mutableStateOf(0f) }
    var flipRotation by remember(transitionTrigger) { mutableStateOf(0f) }
    val animationSpec = tween<Float>(1000, easing = CubicBezierEasing(0.4f, 0.0f, 0.8f, 0.8f))
    val animationSpecFlip = tween<Float>(1000, easing = CubicBezierEasing(0.4f, 0.0f, 0.8f, 0.8f))

    LaunchedEffect(key1 = transitionTrigger) {
        animate(initialValue = 0f, targetValue = 1f, animationSpec = animationSpec) { value: Float, _: Float ->
            offset = value
        }
        animate(initialValue = 0f, targetValue = 180f, animationSpec = animationSpecFlip) { value: Float, _: Float ->
            flipRotation = value
        }
    }

    Layout(
        modifier = modifier.fillMaxSize(),
        content = {
            leftStack(Modifier.layoutId("leftStack"))
            rightStack(Modifier.layoutId("rightStack"))
            flipCard?.let {
                val cardFaceDisplayModifier = Modifier
                    .layoutId("flipCard")
                    .graphicsLayer {
                        rotationY = flipRotation
                        cameraDistance = 8 * density
                    }
                if (flipRotation < 90f) {
                    CardFaceDisplay(flipCard.back, cardFaceDisplayModifier)
                } else {
                    CardFaceDisplay(flipCard.front,
                        modifier = cardFaceDisplayModifier.graphicsLayer { rotationY = 180f }
                    )
                }
            }
        }) { measurable, constraints ->

        val flipCardPlaceable = measurable.firstOrNull { it.layoutId == "flipCard" }
        val leftStackPlaceable = measurable.firstOrNull { it.layoutId == "leftStack" }
        val rightStackPlaceable = measurable.firstOrNull { it.layoutId == "rightStack" }

        layout(constraints.maxWidth, constraints.maxHeight) {
            val cardSpacing = 16.dp.toPx()
            val cardWidth = ((constraints.maxWidth - cardSpacing) / 2).toInt()
            val cardConstraints = constraints.copy(
                minWidth = minOf(constraints.minWidth, cardWidth),
                maxWidth = cardWidth
            )

            val leftStackX = 0
            val rightStackX = leftStackX + cardSpacing + cardWidth
            val flipCardX = rightStackX * offset

            leftStackPlaceable?.measure(cardConstraints)?.place(leftStackX, 0)
            rightStackPlaceable?.measure(cardConstraints)?.place(rightStackX.toInt(), 0)
            flipCardPlaceable?.measure(cardConstraints)?.place(flipCardX.toInt(), 0)
        }
    }
}
