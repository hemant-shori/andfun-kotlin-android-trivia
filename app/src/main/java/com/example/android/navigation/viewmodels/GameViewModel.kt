package com.example.android.navigation.viewmodels

import androidx.lifecycle.ViewModel
import com.example.android.navigation.data.DataSource
import com.example.android.navigation.data.GameUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GameViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()
    val numQuestions = Math.min((DataSource.questions.size + 1) / 2, 3)


    // randomize the questions and set the first question
    fun randomizeQuestions() {
        DataSource.questions.shuffle()
        _uiState.value = GameUiState()
        // Start with first question
        setQuestion(0)
    }

    // Sets the question and randomizes the answers.
    private fun setQuestion(questionIndex: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                currentQuestion = DataSource.questions[questionIndex],
                answers = DataSource.questions[questionIndex]
                    .answers.toMutableList()
                    .apply { shuffle() }, // randomize the answers using shuffle
                questionIndex = questionIndex, // Save the current question index
                selectedAnswer = "" // reset the Selected Answer
            )
        }
    }

    // update the question index.
    private fun setQuestionIndex(questionIndex: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                questionIndex = questionIndex, // Save the current question index
            )
        }
    }

    // Sets the question and randomizes the answers.
    fun setSelectedAnswer(selectedAnswer: String) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedAnswer = selectedAnswer
            )
        }
    }

    fun matchAnswer(gameResultListener: (Boolean) -> Unit) {
        if (_uiState.value.selectedAnswer != "") {
            // The first answer in the original question is always the correct one, so if our
            // answer matches, we have the correct answer.
            if (_uiState.value.selectedAnswer == _uiState.value.currentQuestion.answers[0]) {
                val nextIndex = _uiState.value.questionIndex + 1
                // Advance to the next question
                if (nextIndex < numQuestions) {
                    setQuestion(nextIndex)
                } else {
                    setQuestionIndex(nextIndex)
                    // We've won!  Navigate to the Game won screen.
                    gameResultListener(true)
                }
            } else {
                gameResultListener(false)
                // Game over! A wrong answer sends us to the Game over screen.
            }
        }
    }

}