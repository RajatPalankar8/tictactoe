package com.proto.simpletictactoe.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.proto.simpletictactoe.R
import com.proto.simpletictactoe.ui.theme.NeonColors

@Composable
fun rememberDrawablePainter(@DrawableRes id: Int): Painter {
    val context = LocalContext.current
    return remember(id, context) {
        val drawable = ContextCompat.getDrawable(context, id)
        if (drawable != null) {
            BitmapPainter(drawable.toBitmap().asImageBitmap())
        } else {
            ColorPainter(Color.Transparent)
        }
    }
}

@Composable
fun AppLogoImage(
    modifier: Modifier = Modifier,
    size: Dp = 100.dp,
    @DrawableRes iconResId: Int = R.mipmap.ic_launcher_round
) {
    val cornerRadius = size * 0.22f
    val shape = RoundedCornerShape(cornerRadius)

    Image(
        painter = rememberDrawablePainter(id = iconResId),
        contentDescription = "App Logo",
        modifier = modifier
            .size(size)
            .shadow(
                elevation = 12.dp,
                shape = shape,
                ambientColor = NeonColors.NeonX,
                spotColor = NeonColors.NeonO
            )
            .clip(shape)
            .border(
                width = 2.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        NeonColors.NeonX,
                        NeonColors.Grid,
                        NeonColors.NeonO
                    )
                ),
                shape = shape
            )
    )
}

