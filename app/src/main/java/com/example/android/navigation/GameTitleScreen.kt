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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.android.navigation.ui.theme.AndroidTriviaTheme

@Composable
fun GameTitleContent(onPlayButtonClicked: () -> Unit) {
    // A surface container using the 'background' color from the theme
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceAround,
        ) {
            val commonModifier = Modifier
                .padding(
                    top = dimensionResource(id = R.dimen.vertical_margin),
                    start = dimensionResource(id = R.dimen.horizontal_margin),
                    end = dimensionResource(id = R.dimen.horizontal_margin)
                )
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.android_trivia),
                contentDescription = "",
                modifier = commonModifier
                    .fillMaxWidth()
                    .height(dimensionResource(id = R.dimen.image_header_height))
            )
            Button(
                onClick = onPlayButtonClicked,
                modifier = commonModifier
                    .align(alignment = Alignment.CenterHorizontally)
            ) {
                Text(
                    text = stringResource(id = R.string.play),
                    fontWeight = FontWeight.Bold,
                    fontSize = dimensionResource(id = R.dimen.button_text_size).value.sp,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GameTitlePreview() {
    AndroidTriviaTheme {
        GameTitleContent(
            onPlayButtonClicked = {}
        )
    }
}
