package com.example.lovein.common.models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.lovein.common.data.Action

enum class FeedbackType {
    LIKE,
    DISLIKE,
    NONE
}

data class ActionWithFeedback(
    val action: Action,
    var feedback: MutableState<FeedbackType> = mutableStateOf(FeedbackType.NONE)
) {
    companion object {
        fun fromActions(actions: List<Action>): List<ActionWithFeedback> {
            return actions.map { ActionWithFeedback(it) }
        }
    }
}
