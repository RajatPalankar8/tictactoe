package com.proto.simpletictactae.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "GAME SETUP",
                color = NeonColors.NeonX,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 1. Game Mode
            SectionHeader("SELECT GAME MODE")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
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

            Spacer(modifier = Modifier.height(24.dp))

            // 2. Board Config
            SectionHeader("SELECT BOARD SIZE")
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                val boards = listOf(
                    BoardConfigs.CLASSIC to "3 × 3 (Classic • Get 3)",
                    BoardConfigs.MEGA to "6 × 6 (Mega • Get 4)",
                    BoardConfigs.PRO to "9 × 9 (Pro • Get 5)",
                    BoardConfigs.ULTIMATE to "11 × 11 (Ultimate • Get 5)"
                )
                boards.forEach { (cfg, label) ->
                    OptionCard(
                        text = label,
                        isSelected = selectedConfig == cfg,
                        onClick = { selectedConfig = cfg },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // 3. Difficulty (if Vs Computer)
            if (selectedMode == GameMode.HUMAN_VS_COMPUTER) {
                Spacer(modifier = Modifier.height(24.dp))
                SectionHeader("DIFFICULTY")
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
            Spacer(modifier = Modifier.height(24.dp))
            SectionHeader("YOUR SYMBOL")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
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

            Spacer(modifier = Modifier.height(36.dp))

            // Start & Back
            NeonButton(
                text = "START GAME",
                onClick = {
                    onStartGame(selectedConfig, selectedMode, selectedDifficulty, selectedPlayer)
                },
                neonColor = NeonColors.NeonX
            )

            Spacer(modifier = Modifier.height(12.dp))

            NeonButton(
                text = "BACK TO MENU",
                onClick = onBackClicked,
                neonColor = NeonColors.TextSecondary
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        color = NeonColors.TextSecondary,
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.5.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
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
        Text(
            text = text,
            color = if (isSelected) accentColor else NeonColors.TextPrimary,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            modifier = Modifier.padding(vertical = 14.dp, horizontal = 16.dp)
        )
    }
}
