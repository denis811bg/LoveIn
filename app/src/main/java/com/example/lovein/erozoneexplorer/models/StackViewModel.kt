package com.example.lovein.erozoneexplorer.models

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StackViewModel @Inject constructor() : ViewModel() {
    private lateinit var cards: List<Card>
    private var position: Int = 0

    val leftStackTop
        get() = cards.getOrNull(position + 1)

    val rightStackTop
        get() = cards.getOrNull(position - 1)

    val flipCard
        get() = cards.getOrNull(position)

    fun setPosition(newPosition: Int) {
        position = newPosition
    }

    fun setCards(cards: List<Card>) {
        this.cards = cards
    }
}
