package com.example.lovein.erozoneexplorer.models

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import com.example.lovein.common.models.ActionWithFeedback

sealed class CardFace

class CardFront(
    val content: AnnotatedString,
    val color: Color,
    val actionWithFeedback: ActionWithFeedback
) : CardFace()

class CardBack(val color: Color) : CardFace()
