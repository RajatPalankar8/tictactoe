package com.proto.simpletictactae.domain.engine

import com.proto.simpletictactae.domain.model.Board
import com.proto.simpletictactae.domain.model.BoardConfig
import com.proto.simpletictactae.domain.model.MoveResult
import com.proto.simpletictactae.domain.model.Player
import com.proto.simpletictactae.domain.model.toCell

class GameEngine(
    val config: BoardConfig,
    initialPlayer: Player = Player.X
) {
    val board = Board(config.size)

    var currentPlayer: Player = initialPlayer
        private set

    fun makeMove(row: Int, column: Int): MoveResult {
        if (row !in 0 until config.size || column !in 0 until config.size) {
            return MoveResult.Invalid
        }

        if (!board.isEmpty(row, column)) {
            return MoveResult.Invalid
        }

        board.set(row, column, currentPlayer.toCell())

        val winningCells = WinChecker.findWinningCells(
            board = board,
            row = row,
            column = column,
            player = currentPlayer,
            winLength = config.winLength
        )

        if (winningCells.isNotEmpty()) {
            return MoveResult.Win(
                player = currentPlayer,
                cells = winningCells
            )
        }

        if (board.isFull()) {
            return MoveResult.Draw
        }

        currentPlayer = currentPlayer.opponent()

        return MoveResult.Continue(
            nextPlayer = currentPlayer
        )
    }
}
