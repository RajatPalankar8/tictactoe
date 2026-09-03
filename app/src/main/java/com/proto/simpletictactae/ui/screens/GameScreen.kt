package com.proto.simpletictactae.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.proto.simpletictactae.domain.model.GameMode
import com.proto.simpletictactae.domain.model.GameResult
import com.proto.simpletictactae.domain.model.Player
import com.proto.simpletictactae.ui.components.NeonBackground
import com.proto.simpletictactae.ui.components.NeonBoard
import com.proto.simpletictactae.ui.components.NeonButton
import com.proto.simpletictactae.ui.components.ScoreRow
import com.proto.simpletictactae.ui.components.TurnIndicator
import com.proto.simpletictactae.ui.theme.NeonColors

@Composable
fun GameScreen(
    viewModel: GameViewModel,
    onNavigateToSetup: () -> Unit,
    onNavigateToMenu: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

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
                    text = "TIC TAC TAE",
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
        }

        // Result Overlay
        if (uiState.result != GameResult.InProgress) {
            ResultDialog(
                result = uiState.result,
                gameMode = uiState.gameMode,
                humanPlayer = uiState.humanPlayer,
                onRematch = { viewModel.restartGame() },
                onChangeGame = onNavigateToSetup,
                onMenu = onNavigateToMenu
            )
        }
    }
}

@Composable
private fun ResultDialog(
    result: GameResult,
    gameMode: GameMode,
    humanPlayer: Player,
    onRematch: () -> Unit,
    onChangeGame: () -> Unit,
    onMenu: () -> Unit
) {
    Dialog(onDismissRequest = {}) {
        Card(
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
