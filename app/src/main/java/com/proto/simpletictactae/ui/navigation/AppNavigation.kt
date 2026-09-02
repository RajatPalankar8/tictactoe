package com.proto.simpletictactae.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.proto.simpletictactae.ui.screens.GameScreen
import com.proto.simpletictactae.ui.screens.GameViewModel
import com.proto.simpletictactae.ui.screens.HowToPlayScreen
import com.proto.simpletictactae.ui.screens.MainMenuScreen
import com.proto.simpletictactae.ui.screens.SetupScreen
import com.proto.simpletictactae.ui.screens.SettingsScreen
import com.proto.simpletictactae.ui.screens.StatsScreen

object Routes {
    const val MAIN = "main"
    const val SETUP = "setup"
    const val GAME = "game"
    const val SETTINGS = "settings"
    const val STATS = "stats"
    const val HOW_TO_PLAY = "how_to_play"
}

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    gameViewModel: GameViewModel = viewModel()
) {
    NavHost(
        navController = navController,
        startDestination = Routes.MAIN
    ) {
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
                        popUpTo(Routes.MAIN)
                    }
                },
                onBackClicked = { navController.popBackStack() }
            )
        }

        composable(Routes.GAME) {
            GameScreen(
                viewModel = gameViewModel,
                onNavigateToSetup = { navController.navigate(Routes.SETUP) },
                onNavigateToMenu = {
                    navController.navigate(Routes.MAIN) {
                        popUpTo(Routes.MAIN) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.SETTINGS) {
            SettingsScreen(
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
