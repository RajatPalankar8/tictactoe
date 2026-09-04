package com.proto.simpletictactoe.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.proto.simpletictactoe.ui.screens.GameScreen
import com.proto.simpletictactoe.ui.screens.GameViewModel
import com.proto.simpletictactoe.ui.screens.HowToPlayScreen
import com.proto.simpletictactoe.ui.screens.MainMenuScreen
import com.proto.simpletictactoe.ui.screens.PrivacyPolicyScreen
import com.proto.simpletictactoe.ui.screens.SetupScreen
import com.proto.simpletictactoe.ui.screens.SettingsScreen
import com.proto.simpletictactoe.ui.screens.SplashScreen
import com.proto.simpletictactoe.ui.screens.StatsScreen

object Routes {
    const val SPLASH = "splash"
    const val MAIN = "main"
    const val SETUP = "setup"
    const val GAME = "game"
    const val SETTINGS = "settings"
    const val STATS = "stats"
    const val HOW_TO_PLAY = "how_to_play"
    const val PRIVACY_POLICY = "privacy_policy"
}

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    gameViewModel: GameViewModel = viewModel()
) {
    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {
        composable(Routes.SPLASH) {
            SplashScreen(
                onSplashFinished = {
                    navController.navigate(Routes.MAIN) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.MAIN) {
            MainMenuScreen(
                onPlayClicked = { navController.navigate(Routes.SETUP) },
                onHowToPlayClicked = { navController.navigate(Routes.HOW_TO_PLAY) },
                onStatsClicked = { navController.navigate(Routes.STATS) },
                onSettingsClicked = { navController.navigate(Routes.SETTINGS) }
            )
        }

        composable(Routes.SETUP) {
            SetupScreen(
                onStartGame = { config, mode, difficulty, player ->
                    gameViewModel.startGame(config, mode, difficulty, player, resetScore = true)
                    navController.navigate(Routes.GAME) {
                        popUpTo(Routes.SETUP) { inclusive = true }
                    }
                },
                onBackClicked = { navController.popBackStack() }
            )
        }

        composable(Routes.GAME) {
            GameScreen(
                viewModel = gameViewModel,
                onNavigateToSetup = {
                    gameViewModel.resetGame()
                    navController.navigate(Routes.SETUP) {
                        popUpTo(Routes.GAME) { inclusive = true }
                    }
                },
                onNavigateToMenu = {
                    gameViewModel.resetGame()
                    if (!navController.popBackStack(Routes.MAIN, inclusive = false)) {
                        navController.navigate(Routes.MAIN) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }
            )
        }

        composable(Routes.SETTINGS) {
            SettingsScreen(
                onPrivacyPolicyClicked = { navController.navigate(Routes.PRIVACY_POLICY) },
                onBackClicked = { navController.popBackStack() }
            )
        }

        composable(Routes.PRIVACY_POLICY) {
            PrivacyPolicyScreen(
                onBackClicked = { navController.popBackStack() }
            )
        }

        composable(Routes.STATS) {
            StatsScreen(
                onBackClicked = { navController.popBackStack() }
            )
        }

        composable(Routes.HOW_TO_PLAY) {
            HowToPlayScreen(
                onBackClicked = { navController.popBackStack() }
            )
        }
    }
}
