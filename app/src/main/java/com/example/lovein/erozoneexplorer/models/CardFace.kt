package com.example.lovein.erozoneexplorer.models

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString

sealed class CardFace

class CardFront(
    val content: AnnotatedString,
    val color: Color,
) : CardFace()

class CardBack(val color: Color) : CardFace()
