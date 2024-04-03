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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import com.example.android.navigation.data.DataSource
import com.example.android.navigation.data.GameUiState
import com.example.android.navigation.ui.theme.AndroidTriviaTheme

@Composable
fun PlayGameScreen(
    onOptionSelected: (String) -> Unit,
    onSubmitButtonClicked: () -> Unit,
    uiState: GameUiState
) {

    // A surface container using the 'background' color from the theme
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
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
                text = uiState.currentQuestion.text,
                style = MaterialTheme.typography.titleLarge,
                modifier = commonModifier
            )
            Column(
                modifier = commonModifier
                    .fillMaxWidth()
                    .selectableGroup(),
            ) {

                uiState.answers.forEach { option ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = option == uiState.selectedAnswer,
                                role = Role.RadioButton,
                                onClick = { onOptionSelected(option) }
                            )
                    ) {
                        RadioButton(
                            selected = option == uiState.selectedAnswer,
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
                    onClick = onSubmitButtonClicked,
                ) {
                    Text(text = stringResource(id = R.string.submit_button))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PlayGameScreenPreview() {
    AndroidTriviaTheme {
        PlayGameScreen(
            onSubmitButtonClicked = {},
            onOptionSelected = {},
            uiState = GameUiState(
                currentQuestion = DataSource.questions[1],
                answers = DataSource.questions[1].answers.toMutableList(),
            )
        )
    }
}