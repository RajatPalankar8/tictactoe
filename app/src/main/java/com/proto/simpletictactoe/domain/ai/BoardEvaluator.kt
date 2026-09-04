package com.proto.simpletictactoe.domain.ai

import com.proto.simpletictactoe.domain.model.Board
import com.proto.simpletictactoe.domain.model.Player
import com.proto.simpletictactoe.domain.model.toCell
import kotlin.math.abs

object BoardEvaluator {

    private val directions = listOf(
        0 to 1,   // Horizontal
        1 to 0,   // Vertical
        1 to 1,   // Diagonal
        1 to -1   // Reverse diagonal
    )

    fun evaluate(board: Board, aiPlayer: Player, winLength: Int): Int {
        val aiCell = aiPlayer.toCell()
        val oppCell = aiPlayer.opponent().toCell()
        var totalScore = 0

        // Center bias
        val center = board.size / 2.0
        for (r in 0 until board.size) {
            for (c in 0 until board.size) {
                val cell = board.get(r, c)
                if (cell == aiCell) {
                    val dist = abs(r - center) + abs(c - center)
                    totalScore += (10 - dist.toInt()).coerceAtLeast(1) * 10
                } else if (cell == oppCell) {
                    val dist = abs(r - center) + abs(c - center)
                    totalScore -= (10 - dist.toInt()).coerceAtLeast(1) * 10
                }
            }
        }

        // Window evaluation along directions
        val size = board.size
        for ((dr, dc) in directions) {
            for (r in 0 until size) {
                for (c in 0 until size) {
                    val endR = r + dr * (winLength - 1)
                    val endC = c + dc * (winLength - 1)
                    if (endR in 0 until size && endC in 0 until size) {
                        var aiCount = 0
                        var oppCount = 0
                        for (i in 0 until winLength) {
                            val currCell = board.get(r + dr * i, c + dc * i)
                            if (currCell == aiCell) aiCount++
                            else if (currCell == oppCell) oppCount++
                        }

                        if (aiCount > 0 && oppCount == 0) {
                            totalScore += scoreLine(aiCount, winLength)
                        } else if (oppCount > 0 && aiCount == 0) {
                            totalScore -= scoreLine(oppCount, winLength)
                        }
                    }
                }
            }
        }

        return totalScore
    }

    private fun scoreLine(count: Int, winLength: Int): Int {
        if (count >= winLength) return 1_000_000
        return when (count) {
            winLength - 1 -> 20_000
            winLength - 2 -> 2_000
            winLength - 3 -> 200
            else -> 10
        }
    }
}
