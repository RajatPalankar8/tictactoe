package com.proto.simpletictactoe.ui.screens

import android.content.Context
import android.content.Intent
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.proto.simpletictactoe.R
import com.proto.simpletictactoe.data.GamePreferences
import com.proto.simpletictactoe.data.PreferencesRepository
import com.proto.simpletictactoe.ui.components.NeonBackground
import com.proto.simpletictactoe.ui.components.NeonButton
import com.proto.simpletictactoe.ui.theme.NeonColors
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    onBackClicked: () -> Unit
) {
    val context = LocalContext.current
    val repository = PreferencesRepository(context)
    val preferences by repository.preferencesFlow.collectAsState(initial = GamePreferences())
    val scope = rememberCoroutineScope()

    NeonBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "SETTINGS",
                    color = NeonColors.NeonX,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )

                Spacer(modifier = Modifier.height(32.dp))

                SettingRow(
                    title = "SOUND EFFECTS",
                    checked = preferences.soundEnabled,
                    onCheckedChange = { scope.launch { repository.setSoundEnabled(it) } }
                )

                Spacer(modifier = Modifier.height(16.dp))

                SettingRow(
                    title = "VIBRATION / HAPTICS",
                    checked = preferences.vibrationEnabled,
                    onCheckedChange = { scope.launch { repository.setVibrationEnabled(it) } }
                )

                Spacer(modifier = Modifier.height(16.dp))

                SettingRow(
                    title = "NEON ANIMATIONS",
                    checked = preferences.animationsEnabled,
                    onCheckedChange = { scope.launch { repository.setAnimationsEnabled(it) } }
                )

                Spacer(modifier = Modifier.height(20.dp))

                NeonButton(
                    text = "SHARE WITH FRIENDS",
                    onClick = { shareApp(context) },
                    neonColor = NeonColors.NeonO
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            NeonButton(
                text = "BACK TO MENU",
                onClick = onBackClicked,
                neonColor = NeonColors.TextSecondary
            )
        }
    }
}

private fun shareApp(context: Context) {
    val packageName = context.packageName
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(
            Intent.EXTRA_TEXT,
            context.getString(R.string.share_app_text, packageName)
        )
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, context.getString(R.string.share_app_title))
    context.startActivity(shareIntent)
}

@Composable
private fun SettingRow(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = NeonColors.SurfaceCard),
        border = BorderStroke(1.dp, NeonColors.Grid.copy(alpha = 0.4f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                color = NeonColors.TextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.sp
            )

            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = NeonColors.NeonX,
                    checkedTrackColor = NeonColors.SurfacePanel,
                    uncheckedThumbColor = NeonColors.TextMuted,
                    uncheckedTrackColor = NeonColors.SurfacePanel
                )
            )
        }
    }
}
