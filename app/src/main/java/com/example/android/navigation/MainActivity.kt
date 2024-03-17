package com.example.android.navigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.android.navigation.ui.theme.AndroidTriviaTheme
import com.example.android.navigation.viewmodels.GameViewModel

/*
 * enum class to define the routes.
 */
enum class TriviaAppScreens(@StringRes val title: Int) {
    GameTitleScreen(R.string.app_name),
    GameScreen(R.string.title_android_trivia_question),
    GameWonScreen(R.string.congratulations),
    GameOverScreen(R.string.game_over),
    GameRulesScreen(R.string.rules),
    AboutGameScreen(R.string.about),
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidTriviaTheme {
                ScaffoldRootContent()
            }
        }
    }

    @Composable
    fun ScaffoldRootContent(
        navigationController: NavHostController = rememberNavController(),
        viewModel: GameViewModel = viewModel()
    ) {
        val backStackEntry by navigationController.currentBackStackEntryAsState()
        val currentScreenTitle = TriviaAppScreens.valueOf(
            backStackEntry?.destination?.route ?: TriviaAppScreens.GameTitleScreen.name
        )
        val canNavigateBack = navigationController.previousBackStackEntry != null
        Scaffold(
            containerColor = MaterialTheme.colorScheme.secondary,
            topBar = {
                val uiState by viewModel.uiState.collectAsState()
                GameTopBar(
                    currentScreenTitle,
                    uiState.questionIndex,
                    canNavigateBack,
                    navigateUp = { navigateToHomeScreen(navigationController) }
                )
            },
            modifier = Modifier.background(color = Color.Yellow),
            content = { paddingValues ->
                GameNavHost(navigationController, viewModel, paddingValues)
            }
        )
    }

    @Composable
    private fun GameNavHost(
        navigationController: NavHostController,
        viewModel: GameViewModel,
        paddingValues: PaddingValues
    ) {
        val uiState by viewModel.uiState.collectAsState()
        NavHost(
            navController = navigationController,
            startDestination = TriviaAppScreens.GameTitleScreen.name,
            modifier = Modifier.padding(paddingValues)
        ) {
            // Title Screen Route
            composable(route = TriviaAppScreens.GameTitleScreen.name) {
                GameTitleContent(
                    onPlayButtonClicked = {
                        // Shuffles the questions and sets the question index to the first question.
                        viewModel.randomizeQuestions()
                        navigationController.navigate(TriviaAppScreens.GameScreen.name)
                    }
                )
            }
            // Play Game Route
            composable(route = TriviaAppScreens.GameScreen.name) {
                val gameResultListener: (Boolean) -> Unit = { result ->
                    if (result) {
                        navigationController.navigate(TriviaAppScreens.GameWonScreen.name)
                    } else {
                        navigationController.navigate(TriviaAppScreens.GameOverScreen.name)
                    }
                }
                PlayGameScreen(
                    onOptionSelected = { viewModel.setSelectedAnswer(it) },
                    onSubmitButtonClicked = { viewModel.matchAnswer(gameResultListener) },
                    uiState = uiState
                )
            }
            // Game Win Route
            composable(route = TriviaAppScreens.GameWonScreen.name) {
                GameWonScreen(
                    // Navigate back to title screen
                    nextMatchListener = { navigateToHomeScreen(navigationController) }
                )
            }
            // Game Over Route
            composable(route = TriviaAppScreens.GameOverScreen.name) {
                GameOverScreen(
                    // Navigate back to title screen
                    tryAgainListener = { navigateToHomeScreen(navigationController) }
                )
            }
            // About Game Route
            composable(route = TriviaAppScreens.AboutGameScreen.name) {
                AboutGameScreen()
            }
            // Game Rules Route
            composable(route = TriviaAppScreens.GameRulesScreen.name) {
                GameRulesScreen()
            }
        }
    }

    private fun navigateToHomeScreen(navigationController: NavHostController) {
        // Pop back stack to the game title screen
        navigationController.popBackStack(
            route = TriviaAppScreens.GameTitleScreen.name,
            inclusive = false
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun GameTopBar(
        currentScreenTitle: TriviaAppScreens,
        questionNo: Int,
        canNavigateBack: Boolean,
        navigateUp: () -> Unit
    ) {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(
                        id = currentScreenTitle.title,
                        formatArgs = arrayOf(questionNo + 1, 3)
                    )
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                titleContentColor = MaterialTheme.colorScheme.onSecondary
            ),
            navigationIcon = {
                if (canNavigateBack) {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back_button)
                        )
                    }
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
