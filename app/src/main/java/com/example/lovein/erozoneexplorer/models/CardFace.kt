package com.example.lovein.erozoneexplorer.models

import androidx.compose.ui.graphics.Color

sealed class CardFace

class CardFront(
    val content: String,
    val color: Color,
) : CardFace()

class CardBack(val color: Color) : CardFace()
