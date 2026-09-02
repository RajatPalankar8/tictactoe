package com.proto.simpletictactae.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
fun MainMenuScreen(
    onPlayClicked: () -> Unit,
    onHowToPlayClicked: () -> Unit,
    onStatsClicked: () -> Unit,
    onSettingsClicked: () -> Unit
) {
    NeonBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp, vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Neon Title
            Text(
                text = "TIC TAC TAE",
                color = NeonColors.NeonX,
                fontSize = 38.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 4.sp
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 8.dp, bottom = 48.dp)
            ) {
                Text(
                    text = "NEON X",
                    color = NeonColors.NeonX,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
                Text(
                    text = "•",
                    color = NeonColors.AccentGlow,
                    fontSize = 14.sp
                )
                Text(
                    text = "NEON O",
                    color = NeonColors.NeonO,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
            }

            // Buttons
            NeonButton(
                text = "PLAY",
                onClick = onPlayClicked,
                neonColor = NeonColors.NeonX
            )

            Spacer(modifier = Modifier.height(16.dp))

            NeonButton(
                text = "HOW TO PLAY",
                onClick = onHowToPlayClicked,
                neonColor = NeonColors.Grid
            )

            Spacer(modifier = Modifier.height(16.dp))

            NeonButton(
                text = "STATISTICS",
                onClick = onStatsClicked,
                neonColor = NeonColors.AccentGlow
            )

            Spacer(modifier = Modifier.height(16.dp))

            NeonButton(
                text = "SETTINGS",
                onClick = onSettingsClicked,
                neonColor = NeonColors.TextSecondary
            )
        }
    }
}
