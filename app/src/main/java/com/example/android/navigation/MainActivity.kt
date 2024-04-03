package com.example.android.navigation

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
                    viewModel.numQuestions,
                    navigationController,
                    currentScreenTitle,
                    uiState.questionIndex,
                    canNavigateBack,
                    // Add Support for the Up Button
                    navigateUp = { navigateToHomeScreen(viewModel, navigationController) }
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
                    // solution for exercise 07: Step.07.Exercise-Adding-Safe-Arguments
                    Toast.makeText(
                        applicationContext,
                        "NumCorrect: ${viewModel.uiState.value.questionIndex}," +
                                " NumQuestions: ${viewModel.numQuestions}",
                        Toast.LENGTH_SHORT
                    ).show()
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
                    nextMatchListener = { navigateToHomeScreen(viewModel, navigationController) }
                )
            }
            // Game Over Route
            composable(route = TriviaAppScreens.GameOverScreen.name) {
                GameOverScreen(
                    // Navigate back to title screen
                    tryAgainListener = { navigateToHomeScreen(viewModel, navigationController) }
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

    private fun navigateToHomeScreen(
        viewModel: GameViewModel,
        navigationController: NavHostController
    ) {
        // Hide the share button by shuffling the questions and sets the question
        // index to the first question.
        viewModel.randomizeQuestions()
        // Pop back stack to the game title screen
        navigationController.popBackStack(
            route = TriviaAppScreens.GameTitleScreen.name,
            inclusive = false
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun GameTopBar(
        numQuestions: Int,
        navigationController: NavHostController,
        currentScreenTitle: TriviaAppScreens,
        questionNo: Int,
        canNavigateBack: Boolean,
        navigateUp: () -> Unit
    ) {
        var showMenu by remember { mutableStateOf(false) }
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
            // Add Support for navigation icon that is displayed at the start of the top app bar
            navigationIcon = {
                if (canNavigateBack) {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back_button)
                        )
                    }
                }
            },
            actions = {
                if (numQuestions == questionNo) {
                    IconButton(onClick = {
                        shareSuccess(numQuestions, questionNo)
                    }) {
                        Icon(Icons.Default.Share, contentDescription = "Share")
                    }
                }
                IconButton(onClick = { showMenu = !showMenu }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "More")
                }
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text(text = "About") },
                        onClick = {
                            navigationController.navigate(TriviaAppScreens.AboutGameScreen.name)
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(text = "Rules") },
                        onClick = {
                            navigationController.navigate(TriviaAppScreens.GameRulesScreen.name)
                        }
                    )
                }
            }
        )
    }

    // Starting an Activity with our new Intent
    private fun shareSuccess(numQuestions: Int, numCorrect: Int) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.setType("text/plain")
            .putExtra(
                Intent.EXTRA_TEXT,
                getString(R.string.share_success_text, numCorrect, numQuestions)
            )
        startActivity(shareIntent)
    }

    @Preview(showBackground = true)
    @Composable
    fun RootGamePreview() {
        AndroidTriviaTheme {
            ScaffoldRootContent()
        }
    }

}
