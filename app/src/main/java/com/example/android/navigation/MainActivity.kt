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
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.android.navigation.ui.theme.AndroidTriviaTheme

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
        navigationController: NavHostController = rememberNavController()
    ) {
        val backStackEntry by navigationController.currentBackStackEntryAsState()
        val currentScreenTitle = TriviaAppScreens.valueOf(
            backStackEntry?.destination?.route ?: TriviaAppScreens.GameTitleScreen.name
        )
        val canNavigateBack = navigationController.previousBackStackEntry != null
        val mutableQuestionNo = remember {
            mutableIntStateOf(questionIndex)
        }
        Scaffold(
            containerColor = MaterialTheme.colorScheme.secondary,
            topBar = {
                GameTopBar(
                    currentScreenTitle,
                    mutableQuestionNo,
                    canNavigateBack,
                    navigateUp = { navigateToHomeScreen(navigationController) }
                )
            },
            modifier = Modifier.background(color = Color.Yellow),
            content = { paddingValues ->
                GameNavHost(navigationController, paddingValues)
            }
        )
    }

    @Composable
    private fun GameNavHost(navigationController: NavHostController, paddingValues: PaddingValues) {
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
                        randomizeQuestions()
                        navigationController.navigate(TriviaAppScreens.GameScreen.name)
                    }
                )
            }
            // Play Game Route
            composable(route = TriviaAppScreens.GameScreen.name) {
                PlayGameContent(
                    gameResultListener = { result ->
                        if (result) {
                            navigationController.navigate(TriviaAppScreens.GameWonScreen.name)
                        } else {
                            navigationController.navigate(TriviaAppScreens.GameOverScreen.name)
                        }
                    }
                )
            }
            // Game Win Route
            composable(route = TriviaAppScreens.GameWonScreen.name) {
                GameWonScreen(
                    nextMatchListener = { }
                )
            }
            // Game Over Route
            composable(route = TriviaAppScreens.GameOverScreen.name) {
                GameOverScreen(
                    tryAgainListener = { }
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
        navigationController.popBackStack(
            route = TriviaAppScreens.GameTitleScreen.name,
            inclusive = false
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun GameTopBar(
        currentScreenTitle: TriviaAppScreens,
        mutableQuestionNo: MutableIntState,
        canNavigateBack: Boolean,
        navigateUp: () -> Unit
    ) {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(
                        id = currentScreenTitle.title,
                        formatArgs = arrayOf(mutableQuestionNo.intValue + 1, 3)
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
