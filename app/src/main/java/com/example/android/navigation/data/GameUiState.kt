package com.example.android.navigation.data

/**
 * Represents the UI state of a game, including the current question, available answers, and the index of the current question.
 * @property currentQuestion the current question being displayed in the game UI
 * @property answers a mutable list containing the available answers for the current question
 * @property questionIndex the index of the current question, default value is 0
 */
data class GameUiState(
    val currentQuestion: Question = Question("", mutableListOf("")),
    val answers: MutableList<String> = mutableListOf(""),
    val questionIndex: Int = -1,
    val selectedAnswer: String = ""
)