package com.proto.simpletictactoe.domain.ai

import com.proto.simpletictactoe.domain.engine.WinChecker
import com.proto.simpletictactoe.domain.model.Board
import com.proto.simpletictactoe.domain.model.Move
import com.proto.simpletictactoe.domain.model.Player
import com.proto.simpletictactoe.domain.model.toCell
import kotlin.random.Random

object EasyAi {

    fun findMove(
        board: Board,
        aiPlayer: Player,
        winLength: Int,
        random: Random = Random.Default
    ): Move? {
        val emptyCells = board.getEmptyCells()
        if (emptyCells.isEmpty()) return null

        val chance = random.nextFloat() // 0.0 to 1.0

        // 20% chance to take immediate win
        if (chance < 0.20f) {
            val winMove = findImmediateWin(board, aiPlayer, winLength)
            if (winMove != null) return winMove
        }

        // 20% chance to block immediate loss (0.20 to 0.40)
        if (chance < 0.40f) {
            val blockMove = findImmediateWin(board, aiPlayer.opponent(), winLength)
            if (blockMove != null) {
                return Move(blockMove.row, blockMove.column, aiPlayer)
            }
        }

        // Otherwise (60% or fallback) random move
        val (row, col) = emptyCells.random(random)
        return Move(row, col, aiPlayer)
    }

    fun findImmediateWin(board: Board, player: Player, winLength: Int): Move? {
        val emptyCells = board.getEmptyCells()
        for ((r, c) in emptyCells) {
            val copy = board.copy()
            copy.set(r, c, player.toCell())
            val winning = WinChecker.findWinningCells(copy, r, c, player, winLength)
            if (winning.isNotEmpty()) {
                return Move(r, c, player)
            }
        }
        return null
    }
}
