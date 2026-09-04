package com.proto.simpletictactoe.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.proto.simpletictactoe.domain.model.Player
import com.proto.simpletictactoe.ui.theme.NeonColors

@Composable
fun TurnIndicator(
    currentPlayer: Player,
    isAiThinking: Boolean,
    modifier: Modifier = Modifier
) {
    val playerColor = if (currentPlayer == Player.X) NeonColors.NeonX else NeonColors.NeonO
    val text = if (isAiThinking) "AI IS THINKING..." else "${currentPlayer.name}'S TURN"

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = NeonColors.SurfaceCard),
        border = BorderStroke(1.dp, playerColor.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 20.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(playerColor)
                    .border(2.dp, playerColor.copy(alpha = 0.4f), CircleShape)
            )

            Text(
                text = text,
                color = playerColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp,
                modifier = Modifier.padding(start = 12.dp)
            )
        }
    }
}

@Composable
fun ScoreRow(
    xWins: Int,
    oWins: Int,
    draws: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // X Score
        ScoreBadge(
            label = "X",
            score = xWins,
            color = NeonColors.NeonX
        )

        // Draws
        ScoreBadge(
            label = "DRAWS",
            score = draws,
            color = NeonColors.TextSecondary
        )

        // O Score
        ScoreBadge(
            label = "O",
            score = oWins,
            color = NeonColors.NeonO
        )
    }
}

@Composable
private fun ScoreBadge(
    label: String,
    score: Int,
    color: Color
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = NeonColors.SurfacePanel),
        border = BorderStroke(1.dp, color.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = label,
                color = color,
                fontSize = if (label.length == 1) 22.sp else 12.sp,
                fontWeight = if (label.length == 1) FontWeight.Black else FontWeight.Bold
            )
            Text(
                text = score.toString().padStart(2, '0'),
                color = NeonColors.TextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
