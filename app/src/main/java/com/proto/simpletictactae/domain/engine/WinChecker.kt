package com.proto.simpletictactae.domain.engine

import com.proto.simpletictactae.domain.model.Board
import com.proto.simpletictactae.domain.model.Cell
import com.proto.simpletictactae.domain.model.Player
import com.proto.simpletictactae.domain.model.toCell

object WinChecker {

    private val directions = listOf(
        0 to 1,   // Horizontal
        1 to 0,   // Vertical
        1 to 1,   // Diagonal
        1 to -1   // Reverse diagonal
    )

    fun findWinningCells(
        board: Board,
        row: Int,
        column: Int,
        player: Player,
        winLength: Int
    ): List<Pair<Int, Int>> {
        val target = player.toCell()
        if (board.get(row, column) != target) return emptyList()

        for ((dr, dc) in directions) {
            val positive = collectDirection(board, row, column, dr, dc, target)
            val negative = collectDirection(board, row, column, -dr, -dc, target)

            val completeLine = negative.reversed() + listOf(row to column) + positive

            if (completeLine.size >= winLength) {
                val centerIndex = completeLine.indexOfFirst { it.first == row && it.second == column }

                val start = (centerIndex - winLength + 1).coerceAtLeast(0)
                val maxStart = centerIndex.coerceAtMost(completeLine.size - winLength)

                if (start <= maxStart) {
                    return completeLine.subList(start, start + winLength)
                }
            }
        }

        return emptyList()
    }

    private fun collectDirection(
        board: Board,
        startRow: Int,
        startColumn: Int,
        dr: Int,
        dc: Int,
        target: Cell
    ): List<Pair<Int, Int>> {
        val result = mutableListOf<Pair<Int, Int>>()
        var row = startRow + dr
        var column = startColumn + dc

        while (
            row in 0 until board.size &&
            column in 0 until board.size &&
            board.get(row, column) == target
        ) {
            result.add(row to column)
            row += dr
            column += dc
        }

        return result
    }
}
