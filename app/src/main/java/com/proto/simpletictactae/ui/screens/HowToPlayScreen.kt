package com.proto.simpletictactae.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.proto.simpletictactae.ui.components.NeonBackground
import com.proto.simpletictactae.ui.components.NeonButton
import com.proto.simpletictactae.ui.theme.NeonColors

@Composable
fun HowToPlayScreen(
    onBackClicked: () -> Unit
) {
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
                text = "HOW TO PLAY",
                color = NeonColors.NeonX,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            RuleCard(
                title = "OBJECTIVE",
                description = "Players alternate turns placing their symbol (Neon X or Neon O) on empty board cells. Be the first to align the required consecutive symbols in a line!"
            )

            Spacer(modifier = Modifier.height(16.dp))

            RuleCard(
                title = "BOARD SIZES & WIN CONDITIONS",
                description = "• 3 × 3 Classic: Connect 3 symbols to win\n" +
                        "• 6 × 6 Mega: Connect 4 symbols to win\n" +
                        "• 9 × 9 Pro: Connect 5 symbols to win\n" +
                        "• 11 × 11 Ultimate: Connect 5 symbols to win"
            )

            Spacer(modifier = Modifier.height(16.dp))

            RuleCard(
                title = "WINNING DIRECTIONS",
                description = "Winning lines can be formed in 4 directions:\n" +
                        "1. Horizontal (Row)\n" +
                        "2. Vertical (Column)\n" +
                        "3. Main Diagonal (Top-Left to Bottom-Right)\n" +
                        "4. Anti Diagonal (Top-Right to Bottom-Left)"
            )

            Spacer(modifier = Modifier.height(16.dp))

            RuleCard(
                title = "AI DIFFICULTIES",
                description = "• Easy: Casual gameplay with casual moves\n" +
                        "• Medium: Balanced tactical threats\n" +
                        "• Hard: Unbeatable 3x3 Minimax and candidate search on larger boards"
            )

            Spacer(modifier = Modifier.height(32.dp))

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
private fun RuleCard(title: String, description: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = NeonColors.SurfaceCard),
        border = BorderStroke(1.dp, NeonColors.Grid.copy(alpha = 0.4f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = title,
                color = NeonColors.NeonX,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                color = NeonColors.TextSecondary,
                fontSize = 14.sp,
                lineHeight = 22.sp
            )
        }
    }
}
