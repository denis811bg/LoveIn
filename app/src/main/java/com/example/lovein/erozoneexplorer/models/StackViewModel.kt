package com.example.lovein.erozoneexplorer.models

import androidx.lifecycle.ViewModel
import com.example.lovein.common.models.Player
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StackViewModel @Inject constructor() : ViewModel() {
    private var _cards: List<Pair<Player, Card>> = emptyList()
    private var position: Int = 0

    val leftStackTop: Pair<Player, Card>?
        get() = _cards.getOrNull(position + 1)

    val rightStackTop: Pair<Player, Card>?
        get() = _cards.getOrNull(position - 1)

    val flipCard: Card?
        get() = _cards.getOrNull(position)?.second

    fun setPosition(newPosition: Int) {
        position = newPosition
    }

    fun setCards(cards: List<Pair<Player, Card>>) {
        this._cards = cards
    }
}
