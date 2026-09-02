package com.proto.simpletictactae.ui.screens

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.proto.simpletictactae.domain.model.BoardConfig
import com.proto.simpletictactae.domain.model.BoardConfigs
import com.proto.simpletictactae.domain.model.Difficulty
import com.proto.simpletictactae.domain.model.GameMode
import com.proto.simpletictactae.domain.model.Player
import com.proto.simpletictactae.ui.components.NeonBackground
import com.proto.simpletictactae.ui.components.NeonButton
import com.proto.simpletictactae.ui.theme.NeonColors

@Composable
fun SetupScreen(
    onStartGame: (
        config: BoardConfig,
        mode: GameMode,
        difficulty: Difficulty,
        player: Player
    ) -> Unit,
    onBackClicked: () -> Unit
) {
    var selectedMode by remember { mutableStateOf(GameMode.HUMAN_VS_COMPUTER) }
    var selectedConfig by remember { mutableStateOf(BoardConfigs.CLASSIC) }
    var selectedDifficulty by remember { mutableStateOf(Difficulty.MEDIUM) }
    var selectedPlayer by remember { mutableStateOf(Player.X) }

    NeonBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "GAME SETUP",
                    color = NeonColors.NeonX,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                // 1. Game Mode
                SectionHeader("SELECT GAME MODE")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OptionCard(
                        text = "VS AI",
                        isSelected = selectedMode == GameMode.HUMAN_VS_COMPUTER,
                        onClick = { selectedMode = GameMode.HUMAN_VS_COMPUTER },
                        modifier = Modifier.weight(1f)
                    )
                    OptionCard(
                        text = "VS HUMAN",
                        isSelected = selectedMode == GameMode.HUMAN_VS_HUMAN,
                        onClick = { selectedMode = GameMode.HUMAN_VS_HUMAN },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 2. Board Config
                SectionHeader("SELECT BOARD SIZE")
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OptionCard(
                            text = "3×3 (Get 3)",
                            isSelected = selectedConfig == BoardConfigs.CLASSIC,
                            onClick = { selectedConfig = BoardConfigs.CLASSIC },
                            modifier = Modifier.weight(1f)
                        )
                        OptionCard(
                            text = "6×6 (Get 4)",
                            isSelected = selectedConfig == BoardConfigs.MEGA,
                            onClick = { selectedConfig = BoardConfigs.MEGA },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OptionCard(
                            text = "9×9 (Get 5)",
                            isSelected = selectedConfig == BoardConfigs.PRO,
                            onClick = { selectedConfig = BoardConfigs.PRO },
                            modifier = Modifier.weight(1f)
                        )
                        OptionCard(
                            text = "11×11 (Get 5)",
                            isSelected = selectedConfig == BoardConfigs.ULTIMATE,
                            onClick = { selectedConfig = BoardConfigs.ULTIMATE },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // 3. Difficulty (if Vs Computer)
                if (selectedMode == GameMode.HUMAN_VS_COMPUTER) {
                    Spacer(modifier = Modifier.height(12.dp))
                    SectionHeader("AI DIFFICULTY")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Difficulty.entries.forEach { diff ->
                            OptionCard(
                                text = diff.name,
                                isSelected = selectedDifficulty == diff,
                                onClick = { selectedDifficulty = diff },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                // 4. Your Symbol
                Spacer(modifier = Modifier.height(12.dp))
                SectionHeader("YOUR SYMBOL")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OptionCard(
                        text = "X",
                        isSelected = selectedPlayer == Player.X,
                        onClick = { selectedPlayer = Player.X },
                        accentColor = NeonColors.NeonX,
                        modifier = Modifier.weight(1f)
                    )
                    OptionCard(
                        text = "O",
                        isSelected = selectedPlayer == Player.O,
                        onClick = { selectedPlayer = Player.O },
                        accentColor = NeonColors.NeonO,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Start & Back
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                NeonButton(
                    text = "BACK",
                    onClick = onBackClicked,
                    neonColor = NeonColors.TextSecondary,
                    modifier = Modifier.weight(1f)
                )

                NeonButton(
                    text = "START GAME",
                    onClick = {
                        onStartGame(selectedConfig, selectedMode, selectedDifficulty, selectedPlayer)
                    },
                    neonColor = NeonColors.NeonX,
                    modifier = Modifier.weight(1.3f)
                )
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        color = NeonColors.TextSecondary,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.2.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp)
    )
}

@Composable
private fun OptionCard(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    accentColor: Color = NeonColors.NeonX
) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) NeonColors.SurfaceCard else NeonColors.SurfacePanel
        ),
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) accentColor else NeonColors.TextMuted.copy(alpha = 0.3f)
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = if (isSelected) accentColor else NeonColors.TextPrimary,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            )
        }
    }
}
