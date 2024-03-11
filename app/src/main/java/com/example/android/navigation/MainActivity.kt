package com.example.android.navigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.android.navigation.ui.theme.AndroidTriviaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        screenTitle = getString(R.string.app_name)
        setContent {
            AndroidTriviaTheme {
                ScaffoldRootContent()
            }
        }
    }

    companion object {
        lateinit var screenTitle: String
    }

    @Composable
    fun Greeting(name: String) {
        Text(
            text = "Hello $name!"
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ScaffoldRootContent() {
        val mutableScreenTitle = remember {
            mutableStateOf(screenTitle)
        }
        Scaffold(
            containerColor = MaterialTheme.colorScheme.secondary,
            topBar = {
                TopAppBar(
                    title = { Text(text = mutableScreenTitle.value) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        titleContentColor = MaterialTheme.colorScheme.onSecondary
                    )
                )
            },
            modifier = Modifier.background(color = Color.Yellow),
            content = { paddingValues ->

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Test")
                }
            }
        )
    }

    @Preview(showBackground = true)
    @Composable
    fun RootGamePreview() {
        AndroidTriviaTheme {
            ScaffoldRootContent()
        }
    }

}
