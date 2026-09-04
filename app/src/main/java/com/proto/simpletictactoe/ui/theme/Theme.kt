package com.proto.simpletictactoe.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = NeonColors.NeonX,
    secondary = NeonColors.NeonO,
    tertiary = NeonColors.AccentGlow,
    background = NeonColors.Background,
    surface = NeonColors.SurfacePanel,
    onPrimary = NeonColors.Background,
    onSecondary = NeonColors.Background,
    onBackground = NeonColors.TextPrimary,
    onSurface = NeonColors.TextPrimary
)

@Composable
fun SimpleTicTacTaeTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
