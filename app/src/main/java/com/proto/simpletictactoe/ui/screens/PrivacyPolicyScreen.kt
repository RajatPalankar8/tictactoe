package com.proto.simpletictactoe.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.proto.simpletictactoe.ui.components.NeonBackground
import com.proto.simpletictactoe.ui.components.NeonButton
import com.proto.simpletictactoe.ui.theme.NeonColors

@Composable
fun PrivacyPolicyScreen(
    onBackClicked: () -> Unit
) {
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
                text = "ABOUT & PRIVACY",
                color = NeonColors.NeonX,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            PolicyCard(
                title = "ABOUT TIC TAC TOE",
                content = "Tic Tac Toe is a modern, futuristic neon strategy game developed by Palankar.R. Enjoy single-player AI challenges (Easy, Medium, Hard) and local 2-player matches across 3x3, 6x6, 9x9, and 11x11 boards completely free of charge!"
            )

            Spacer(modifier = Modifier.height(16.dp))

            PolicyCard(
                title = "PRIVACY & DATA COLLECTION",
                content = "We value your privacy. We do not collect or store personal identifiers such as your name, email, or contacts. The app utilizes trusted services (Google Play Services, AdMob, and Firebase Analytics) to serve non-intrusive ads and collect anonymous performance logs."
            )

            Spacer(modifier = Modifier.height(16.dp))

            PolicyCard(
                title = "ADVERTISING & ANALYTICS",
                content = "• Google AdMob: Serves banner and interstitial ads using anonymous device IDs to keep the game free to play.\n• Firebase Analytics: Helps us understand game statistics and crash reports to continuously improve gameplay."
            )

            Spacer(modifier = Modifier.height(16.dp))

            PolicyCard(
                title = "CHILDREN'S PRIVACY",
                content = "This app does not knowingly collect or target personal information from children under the age of 13."
            )

            Spacer(modifier = Modifier.height(16.dp))

            PolicyCard(
                title = "CONTACT & APP INFO",
                content = "Developer: Palankar.R\nApplication ID: com.proto.simpletictactoe"
            )

            Spacer(modifier = Modifier.height(32.dp))

            NeonButton(
                text = "BACK TO SETTINGS",
                onClick = onBackClicked,
                neonColor = NeonColors.TextSecondary
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun PolicyCard(title: String, content: String) {
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
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = content,
                color = NeonColors.TextSecondary,
                fontSize = 14.sp,
                lineHeight = 22.sp
            )
        }
    }
}
