package com.proto.simpletictactoe.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.proto.simpletictactoe.domain.model.Board
import com.proto.simpletictactoe.domain.model.Cell
import com.proto.simpletictactoe.ui.theme.NeonColors
import com.proto.simpletictactoe.util.AnimationDurations

@Composable
fun NeonBoard(
    board: Board,
    winningCells: List<Pair<Int, Int>>,
    enabled: Boolean,
    onCellClick: (row: Int, column: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val winProgress = remember { Animatable(0f) }

    LaunchedEffect(winningCells) {
        if (winningCells.isNotEmpty()) {
            winProgress.snapTo(0f)
            winProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = AnimationDurations.WIN_LINE)
            )
        } else {
            winProgress.snapTo(0f)
        }
    }

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .padding(12.dp)
            .pointerInput(board.size, enabled) {
                if (enabled) {
                    detectTapGestures { offset ->
                        val cellWidth = size.width / board.size.toFloat()
                        val cellHeight = size.height / board.size.toFloat()

                        val col = (offset.x / cellWidth).toInt().coerceIn(0, board.size - 1)
                        val row = (offset.y / cellHeight).toInt().coerceIn(0, board.size - 1)

                        onCellClick(row, col)
                    }
                }
            }
    ) {
        val sizeN = board.size
        val cellW = size.width / sizeN.toFloat()
        val cellH = size.height / sizeN.toFloat()

        // 1. Draw Grid Lines
        val gridStroke = when {
            sizeN <= 3 -> 6f
            sizeN <= 6 -> 4f
            else -> 2.5f
        }

        for (i in 1 until sizeN) {
            val x = i * cellW
            // Vertical outer glow & main line
            drawLine(
                color = NeonColors.Grid.copy(alpha = 0.25f),
                start = Offset(x, 0f),
                end = Offset(x, size.height),
                strokeWidth = gridStroke * 2.5f,
                cap = StrokeCap.Round
            )
            drawLine(
                color = NeonColors.Grid,
                start = Offset(x, 0f),
                end = Offset(x, size.height),
                strokeWidth = gridStroke,
                cap = StrokeCap.Round
            )

            val y = i * cellH
            // Horizontal outer glow & main line
            drawLine(
                color = NeonColors.Grid.copy(alpha = 0.25f),
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = gridStroke * 2.5f,
                cap = StrokeCap.Round
            )
            drawLine(
                color = NeonColors.Grid,
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = gridStroke,
                cap = StrokeCap.Round
            )
        }

        // 2. Draw Cell Symbols
        val symbolPadding = cellW * 0.20f
        val symbolStroke = when {
            sizeN <= 3 -> 12f
            sizeN <= 6 -> 8f
            sizeN <= 9 -> 5f
            else -> 3.5f
        }

        for (r in 0 until sizeN) {
            for (c in 0 until sizeN) {
                val cell = board.get(r, c)
                if (cell != Cell.EMPTY) {
                    val isWinningCell = winningCells.contains(r to c)
                    val left = c * cellW + symbolPadding
                    val top = r * cellH + symbolPadding
                    val right = (c + 1) * cellW - symbolPadding
                    val bottom = (r + 1) * cellH - symbolPadding

                    if (cell == Cell.X) {
                        drawNeonX(
                            left = left,
                            top = top,
                            right = right,
                            bottom = bottom,
                            strokeWidth = symbolStroke,
                            isWinning = isWinningCell
                        )
                    } else if (cell == Cell.O) {
                        drawNeonO(
                            left = left,
                            top = top,
                            right = right,
                            bottom = bottom,
                            strokeWidth = symbolStroke,
                            isWinning = isWinningCell
                        )
                    }
                }
            }
        }

        // 3. Draw Winning Line
        if (winningCells.size >= 2) {
            val first = winningCells.first()
            val last = winningCells.last()

            val startX = first.second * cellW + cellW / 2f
            val startY = first.first * cellH + cellH / 2f

            val endX = last.second * cellW + cellW / 2f
            val endY = last.first * cellH + cellH / 2f

            val currentEndX = startX + (endX - startX) * winProgress.value
            val currentEndY = startY + (endY - startY) * winProgress.value

            val winStroke = symbolStroke * 1.8f

            // Glow
            drawLine(
                color = NeonColors.WinLine.copy(alpha = 0.4f),
                start = Offset(startX, startY),
                end = Offset(currentEndX, currentEndY),
                strokeWidth = winStroke * 3f,
                cap = StrokeCap.Round
            )
            // Core Line
            drawLine(
                color = NeonColors.WinLine,
                start = Offset(startX, startY),
                end = Offset(currentEndX, currentEndY),
                strokeWidth = winStroke,
                cap = StrokeCap.Round
            )
        }
    }
}

private fun DrawScope.drawNeonX(
    left: Float,
    top: Float,
    right: Float,
    bottom: Float,
    strokeWidth: Float,
    isWinning: Boolean
) {
    val color = if (isWinning) NeonColors.WinLine else NeonColors.NeonX

    // Outer Dim Glow
    drawLine(
        color = color.copy(alpha = 0.25f),
        start = Offset(left, top),
        end = Offset(right, bottom),
        strokeWidth = strokeWidth * 2.8f,
        cap = StrokeCap.Round
    )
    drawLine(
        color = color.copy(alpha = 0.25f),
        start = Offset(right, top),
        end = Offset(left, bottom),
        strokeWidth = strokeWidth * 2.8f,
        cap = StrokeCap.Round
    )

    // Main Sharp Line
    drawLine(
        color = color,
        start = Offset(left, top),
        end = Offset(right, bottom),
        strokeWidth = strokeWidth,
        cap = StrokeCap.Round
    )
    drawLine(
        color = color,
        start = Offset(right, top),
        end = Offset(left, bottom),
        strokeWidth = strokeWidth,
        cap = StrokeCap.Round
    )
}

private fun DrawScope.drawNeonO(
    left: Float,
    top: Float,
    right: Float,
    bottom: Float,
    strokeWidth: Float,
    isWinning: Boolean
) {
    val color = if (isWinning) NeonColors.WinLine else NeonColors.NeonO
    val width = right - left
    val height = bottom - top

    // Outer Dim Glow
    drawArc(
        color = color.copy(alpha = 0.25f),
        startAngle = 0f,
        sweepAngle = 360f,
        useCenter = false,
        topLeft = Offset(left, top),
        size = Size(width, height),
        style = Stroke(width = strokeWidth * 2.8f)
    )

    // Main Sharp Circle
    drawArc(
        color = color,
        startAngle = 0f,
        sweepAngle = 360f,
        useCenter = false,
        topLeft = Offset(left, top),
        size = Size(width, height),
        style = Stroke(width = strokeWidth)
    )
}
