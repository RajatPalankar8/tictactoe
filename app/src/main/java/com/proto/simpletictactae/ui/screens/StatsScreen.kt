package com.proto.simpletictactae.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.proto.simpletictactae.data.Statistics
import com.proto.simpletictactae.data.StatisticsRepository
import com.proto.simpletictactae.ui.components.NeonBackground
import com.proto.simpletictactae.ui.components.NeonButton
import com.proto.simpletictactae.ui.theme.NeonColors
import kotlinx.coroutines.launch

@Composable
fun StatsScreen(
    onBackClicked: () -> Unit
) {
    val context = LocalContext.current
    val repository = StatisticsRepository(context)
    val stats by repository.statisticsFlow.collectAsState(initial = Statistics())
    val scope = rememberCoroutineScope()

    NeonBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "YOUR STATISTICS",
                color = NeonColors.NeonX,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Summary Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard("GAMES", stats.gamesPlayed.toString(), NeonColors.TextPrimary, Modifier.weight(1f))
                StatCard("WINS", stats.wins.toString(), NeonColors.NeonX, Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard("LOSSES", stats.losses.toString(), NeonColors.NeonO, Modifier.weight(1f))
                StatCard("DRAWS", stats.draws.toString(), NeonColors.TextSecondary, Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Symbols
            Text(
                text = "SYMBOL BREAKDOWN",
                color = NeonColors.TextSecondary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp,
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard("X WINS", stats.xWins.toString(), NeonColors.NeonX, Modifier.weight(1f))
                StatCard("O WINS", stats.oWins.toString(), NeonColors.NeonO, Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Boards
            Text(
                text = "BOARD MODES PLAYED",
                color = NeonColors.TextSecondary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp,
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                BoardStatRow("3 × 3 Classic", stats.classicGames)
                BoardStatRow("6 × 6 Mega", stats.megaGames)
                BoardStatRow("9 × 9 Pro", stats.proGames)
                BoardStatRow("11 × 11 Ultimate", stats.ultimateGames)
            }

            Spacer(modifier = Modifier.height(32.dp))

            NeonButton(
                text = "RESET STATISTICS",
                onClick = { scope.launch { repository.resetStatistics() } },
                neonColor = NeonColors.NeonO
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
private fun StatCard(
    label: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = NeonColors.SurfaceCard),
        border = BorderStroke(1.dp, color.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                color = color,
                fontSize = 26.sp,
                fontWeight = FontWeight.Black
            )
            Text(
                text = label,
                color = NeonColors.TextMuted,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
private fun BoardStatRow(name: String, count: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = NeonColors.SurfacePanel),
        border = BorderStroke(1.dp, NeonColors.Grid.copy(alpha = 0.25f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = name,
                color = NeonColors.TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "$count games",
                color = NeonColors.TextSecondary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
