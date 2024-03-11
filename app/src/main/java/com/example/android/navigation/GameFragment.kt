/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.navigation


import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import com.example.android.navigation.ui.theme.AndroidTriviaTheme

class GameFragment : Fragment() {
    data class Question(
        val text: String, val answers: List<String>
    )

    // The first answer is the correct one.  We randomize the answers before showing the text.
    // All questions must have four answers.  We'd want these to contain references to string
    // resources so we could internationalize. (or better yet, not define the questions in code...)
    private val questions: MutableList<Question> = mutableListOf(
        Question(
            text = "What is Android Jetpack?",
            answers = listOf("all of these", "tools", "documentation", "libraries")
        ), Question(
            text = "Base class for Layout?",
            answers = listOf("ViewGroup", "ViewSet", "ViewCollection", "ViewRoot")
        ), Question(
            text = "Layout for complex Screens?",
            answers = listOf("ConstraintLayout", "GridLayout", "LinearLayout", "FrameLayout")
        ), Question(
            text = "Pushing structured data into a Layout?",
            answers = listOf("Data Binding", "Data Pushing", "Set Text", "OnClick")
        ), Question(
            text = "Inflate layout in fragments?",
            answers = listOf("onCreateView", "onViewCreated", "onCreateLayout", "onInflateLayout")
        ), Question(
            text = "Build system for Android?",
            answers = listOf("Gradle", "Graddle", "Grodle", "Groyle")
        ), Question(
            text = "Android vector format?", answers = listOf(
                "VectorDrawable", "AndroidVectorDrawable", "DrawableVector", "AndroidVector"
            )
        ), Question(
            text = "Android Navigation Component?",
            answers = listOf("NavController", "NavCentral", "NavMaster", "NavSwitcher")
        ), Question(
            text = "Registers app with launcher?",
            answers = listOf("intent-filter", "app-registry", "launcher-registry", "app-launcher")
        ), Question(
            text = "Mark a layout for Data Binding?",
            answers = listOf("<layout>", "<binding>", "<data-binding>", "<dbinding>")
        )
    )

    private lateinit var currentQuestion: Question
    private lateinit var answers: MutableList<String>
    private var questionIndex = 0
    private val numQuestions = Math.min((questions.size + 1) / 2, 3)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Shuffles the questions and sets the question index to the first question.
        randomizeQuestions()

        return ComposeView(requireContext()).apply {
            setContent {

                AndroidTriviaTheme {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        GameContent()
                    }
                }
            }
        }
    }

    @Composable
    fun GameContent() {
        val mutableCurrentQuestion = remember {
            mutableStateOf(currentQuestion)
        }

        val selectedAnswer = remember { mutableStateOf("") }

        Column {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.android_category_simple),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = dimensionResource(id = R.dimen.vertical_margin),
                        start = dimensionResource(id = R.dimen.horizontal_margin),
                        end = dimensionResource(id = R.dimen.horizontal_margin)
                    )
                    .height(dimensionResource(id = R.dimen.image_header_height))
            )

            val commonModifier = Modifier
                .padding(
                    top = dimensionResource(id = R.dimen.horizontal_margin),
                    start = dimensionResource(id = R.dimen.question_horizontal_margin),
                    end = dimensionResource(id = R.dimen.question_horizontal_margin)
                )
            Text(
                text = mutableCurrentQuestion.value.text,
                style = MaterialTheme.typography.titleLarge,
                modifier = commonModifier
            )
            Column(
                modifier = commonModifier
                    .fillMaxWidth()
                    .selectableGroup(),
            ) {

                mutableCurrentQuestion.value.answers.forEach { option ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = option == selectedAnswer.value,
                                role = Role.RadioButton,
                                onClick = {
                                    selectedAnswer.value = option
                                }
                            )
                    ) {
                        RadioButton(
                            selected = option == selectedAnswer.value,
                            onClick = null, // null recommended for accessibility with screen readers
                            modifier = Modifier
                                .padding(dimensionResource(id = R.dimen.horizontal_margin))

                        )
                        Text(
                            text = option,
                            modifier = Modifier
                                .padding(dimensionResource(id = R.dimen.horizontal_margin))
                        )
                    }
                }
                Button(
                    modifier = commonModifier.align(alignment = Alignment.CenterHorizontally),
                    onClick = { submitButtOnClickListener(selectedAnswer) },
                ) {
                    Text(text = stringResource(id = R.string.submit_button))
                }
            }
        }
    }

    private fun submitButtOnClickListener(selectedAnswer: MutableState<String>) {
        if (!selectedAnswer.equals("")) {
            // The first answer in the original question is always the correct one, so if our
            // answer matches, we have the correct answer.
            if (selectedAnswer.value == currentQuestion.answers[0]) {
                questionIndex++
                // Advance to the next question
                if (questionIndex < numQuestions) {
                    currentQuestion = questions[questionIndex]
                    setQuestion()
                } else {
                    // We've won!  Navigate to the gameWonFragment.
                }
            } else {
                // Game over! A wrong answer sends us to the gameOverFragment.
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun GameOverPreview() {
        AndroidTriviaTheme {
            // A surface container using the 'background' color from the theme
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                GameContent()
            }
        }
    }

    // randomize the questions and set the first question
    private fun randomizeQuestions() {
        questions.shuffle()
        questionIndex = 0
        setQuestion()
    }

    // Sets the question and randomizes the answers.  This only changes the data, not the UI.
    // Calling invalidateAll on the FragmentGameBinding updates the data.
    private fun setQuestion() {
        currentQuestion = questions[questionIndex]
        // randomize the answers into a copy of the array
        answers = currentQuestion.answers.toMutableList()
        // and shuffle them
        answers.shuffle()
        // TODO Fix top bar title in step 2 while adding support to navigation compose.
        // Updating the MainActivity screenTitle which is used as TopAppBar title
        // MainActivity.screenTitle = getString(R.string.title_android_trivia_question, questionIndex + 1, numQuestions)
    }
}
