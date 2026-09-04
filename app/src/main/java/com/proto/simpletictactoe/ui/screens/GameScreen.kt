package com.proto.simpletictactoe.ui.screens

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.proto.simpletictactoe.domain.model.GameMode
import com.proto.simpletictactoe.domain.model.GameResult
import com.proto.simpletictactoe.domain.model.Player
import com.proto.simpletictactoe.ui.components.NeonBackground
import com.proto.simpletictactoe.ui.components.NeonBoard
import com.proto.simpletictactoe.ui.components.NeonButton
import com.proto.simpletictactoe.ui.components.ScoreRow
import com.proto.simpletictactoe.ui.components.TurnIndicator
import com.proto.simpletictactoe.ui.theme.NeonColors
import com.proto.simpletictactoe.util.AdMobManager
import com.proto.simpletictactoe.util.BannerAdView
import kotlinx.coroutines.delay

@Composable
fun GameScreen(
    viewModel: GameViewModel,
    onNavigateToSetup: () -> Unit,
    onNavigateToMenu: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val activity = context as? Activity
    var isAdLoadingOverlayVisible by remember { mutableStateOf(false) }

    BackHandler {
        viewModel.resetGame()
        onNavigateToMenu()
    }

    LaunchedEffect(uiState.result) {
        if (uiState.result != GameResult.InProgress && activity != null) {
            if (AdMobManager.isAdReady()) {
                isAdLoadingOverlayVisible = true
                delay(1000)
                isAdLoadingOverlayVisible = false
                AdMobManager.showInterstitialAd(activity)
            } else {
                AdMobManager.showInterstitialAd(activity)
            }
        }
    }

    NeonBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "TIC TAC TOE",
                    color = NeonColors.NeonX,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )

                Card(
                    modifier = Modifier.clickable { viewModel.toggleSound() },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = NeonColors.SurfaceCard),
                    border = BorderStroke(
                        width = 1.dp,
                        color = if (uiState.soundEnabled) NeonColors.NeonX.copy(alpha = 0.6f) else NeonColors.TextMuted.copy(alpha = 0.3f)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = if (uiState.soundEnabled) "🔊 SOUND ON" else "🔇 SOUND OFF",
                            color = if (uiState.soundEnabled) NeonColors.NeonX else NeonColors.TextMuted,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }

            // Scores
            ScoreRow(
                xWins = uiState.xMatchWins,
                oWins = uiState.oMatchWins,
                draws = uiState.matchDraws
            )

            // Turn Indicator
            TurnIndicator(
                currentPlayer = uiState.currentPlayer,
                isAiThinking = uiState.isAiThinking
            )

            // Board
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                NeonBoard(
                    board = uiState.board,
                    winningCells = uiState.winningCells,
                    enabled = uiState.result == GameResult.InProgress && !uiState.isAiThinking,
                    onCellClick = { r, c -> viewModel.onCellClicked(r, c) }
                )
            }

            // Bottom Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                NeonButton(
                    text = "RESTART",
                    onClick = { viewModel.restartGame() },
                    neonColor = NeonColors.NeonX,
                    modifier = Modifier.weight(1f)
                )

                NeonButton(
                    text = "CHANGE MODE",
                    onClick = onNavigateToSetup,
                    neonColor = NeonColors.TextSecondary,
                    modifier = Modifier.weight(1f)
                )
            }

            BannerAdView(
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        // Result Overlay
        if (uiState.result != GameResult.InProgress) {
            ResultOverlay(
                result = uiState.result,
                gameMode = uiState.gameMode,
                humanPlayer = uiState.humanPlayer,
                onRematch = { viewModel.restartGame() },
                onChangeGame = onNavigateToSetup,
                onMenu = onNavigateToMenu
            )
        }

        // Ad Loading Overlay
        if (isAdLoadingOverlayVisible) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.8f))
                    .clickable(enabled = false) {},
                contentAlignment = Alignment.Center
            ) {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = NeonColors.SurfaceCard),
                    border = BorderStroke(1.dp, NeonColors.NeonX.copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 32.dp, vertical = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            color = NeonColors.NeonX,
                            modifier = Modifier.size(36.dp),
                            strokeWidth = 3.dp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "LOADING AD...",
                            color = NeonColors.TextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ResultOverlay(
    result: GameResult,
    gameMode: GameMode,
    humanPlayer: Player,
    onRematch: () -> Unit,
    onChangeGame: () -> Unit,
    onMenu: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.75f))
            .clickable(enabled = false) {},
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.padding(24.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = NeonColors.SurfaceCard),
            border = BorderStroke(
                2.dp,
                when (result) {
                    is GameResult.Winner -> if (result.player == Player.X) NeonColors.NeonX else NeonColors.NeonO
                    else -> NeonColors.TextSecondary
                }
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val title: String
                val subtitle: String
                val color: Color

                when (result) {
                    is GameResult.Winner -> {
                        color = if (result.player == Player.X) NeonColors.NeonX else NeonColors.NeonO
                        if (gameMode == GameMode.HUMAN_VS_COMPUTER) {
                            if (result.player == humanPlayer) {
                                subtitle = "✦ VICTORY ✦"
                                title = "YOU WON!"
                            } else {
                                subtitle = "GAME OVER"
                                title = "AI WON!"
                            }
                        } else {
                            subtitle = "✦ VICTORY ✦"
                            title = "PLAYER ${result.player.name} WINS!"
                        }
                    }
                    is GameResult.Draw -> {
                        title = "DRAW!"
                        subtitle = "PERFECTLY MATCHED"
                        color = NeonColors.TextSecondary
                    }
                    else -> {
                        title = ""
                        subtitle = ""
                        color = NeonColors.TextPrimary
                    }
                }

                Text(
                    text = subtitle,
                    color = color.copy(alpha = 0.8f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )

                Text(
                    text = title,
                    color = color,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                NeonButton(text = "REMATCH", onClick = onRematch, neonColor = color)
                Spacer(modifier = Modifier.height(12.dp))
                NeonButton(text = "CHANGE MODE", onClick = onChangeGame, neonColor = NeonColors.Grid)
                Spacer(modifier = Modifier.height(12.dp))
                NeonButton(text = "MAIN MENU", onClick = onMenu, neonColor = NeonColors.TextSecondary)
            }
        }
    }
}
