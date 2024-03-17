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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.android.navigation.ui.theme.AndroidTriviaTheme

@Composable
fun GameRulesScreen() {
    // A surface container using the 'background' color from the theme
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val scrollState = rememberScrollState()
        Column(
            Modifier.verticalScroll(state = scrollState, enabled = true)
        ) {
            val commonModifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = dimensionResource(id = R.dimen.vertical_margin),
                    start = dimensionResource(id = R.dimen.horizontal_margin),
                    end = dimensionResource(id = R.dimen.horizontal_margin)
                )
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.trivia_rules),
                contentDescription = "",
                modifier = commonModifier.height(dimensionResource(id = R.dimen.image_header_height))
            )
            Text(
                text = stringResource(id = R.string.rules_text),
                style = MaterialTheme.typography.bodyLarge,
                modifier = commonModifier
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GameRulesPreview() {
    AndroidTriviaTheme {
        GameRulesScreen()
    }
}