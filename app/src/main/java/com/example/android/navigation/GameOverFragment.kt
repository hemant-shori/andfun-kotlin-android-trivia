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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import com.example.android.navigation.ui.theme.AndroidTriviaTheme

class GameOverFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                AndroidTriviaTheme {
                    GameOverContent()
                }
            }
        }
    }

    @Composable
    fun GameOverContent() {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = colorResource(id = R.color.gameOverBackground)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
            ) {
                val commonModifier = Modifier
                    .padding(
                        top = dimensionResource(id = R.dimen.vertical_margin),
                        start = dimensionResource(id = R.dimen.horizontal_margin),
                        end = dimensionResource(id = R.dimen.horizontal_margin)
                    )
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.try_again),
                    contentDescription = "",
                    modifier = commonModifier
                        .fillMaxWidth()
                        .height(dimensionResource(id = R.dimen.game_over_height))
                )
                Button(
                    onClick = { /*TODO*/ },
                    modifier = commonModifier
                        .align(alignment = Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = stringResource(id = R.string.try_again),
                        fontWeight = FontWeight.Bold,
                        fontSize = dimensionResource(id = R.dimen.button_text_size).value.sp,
                    )
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun GameOverPreview() {
        AndroidTriviaTheme {
            GameOverContent()
        }
    }
}
