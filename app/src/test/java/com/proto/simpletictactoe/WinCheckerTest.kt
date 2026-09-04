package com.proto.simpletictactoe

import com.proto.simpletictactoe.domain.engine.WinChecker
import com.proto.simpletictactoe.domain.model.Board
import com.proto.simpletictactoe.domain.model.Player
import com.proto.simpletictactoe.domain.model.toCell
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class WinCheckerTest {

    @Test
    fun test3x3HorizontalWin() {
        val board = Board(3)
        board.set(1, 0, Player.X.toCell())
        board.set(1, 1, Player.X.toCell())
        board.set(1, 2, Player.X.toCell())

        val winningCells = WinChecker.findWinningCells(
            board = board,
            row = 1,
            column = 2,
            player = Player.X,
            winLength = 3
        )

        assertEquals(3, winningCells.size)
        assertEquals(listOf(1 to 0, 1 to 1, 1 to 2), winningCells)
    }

    @Test
    fun test3x3VerticalWin() {
        val board = Board(3)
        board.set(0, 1, Player.O.toCell())
        board.set(1, 1, Player.O.toCell())
        board.set(2, 1, Player.O.toCell())

        val winningCells = WinChecker.findWinningCells(
            board = board,
            row = 2,
            column = 1,
            player = Player.O,
            winLength = 3
        )

        assertEquals(3, winningCells.size)
        assertEquals(listOf(0 to 1, 1 to 1, 2 to 1), winningCells)
    }

    @Test
    fun test3x3DiagonalWin() {
        val board = Board(3)
        board.set(0, 0, Player.X.toCell())
        board.set(1, 1, Player.X.toCell())
        board.set(2, 2, Player.X.toCell())

        val winningCells = WinChecker.findWinningCells(
            board = board,
            row = 1,
            column = 1,
            player = Player.X,
            winLength = 3
        )

        assertEquals(3, winningCells.size)
        assertEquals(listOf(0 to 0, 1 to 1, 2 to 2), winningCells)
    }

    @Test
    fun test3x3ReverseDiagonalWin() {
        val board = Board(3)
        board.set(0, 2, Player.X.toCell())
        board.set(1, 1, Player.X.toCell())
        board.set(2, 0, Player.X.toCell())

        val winningCells = WinChecker.findWinningCells(
            board = board,
            row = 0,
            column = 2,
            player = Player.X,
            winLength = 3
        )

        assertEquals(3, winningCells.size)
        assertEquals(listOf(0 to 2, 1 to 1, 2 to 0), winningCells)
    }

    @Test
    fun test6x6WinLength4() {
        val board = Board(6)
        board.set(2, 1, Player.X.toCell())
        board.set(2, 2, Player.X.toCell())
        board.set(2, 3, Player.X.toCell())
        board.set(2, 4, Player.X.toCell())

        val winningCells = WinChecker.findWinningCells(
            board = board,
            row = 2,
            column = 3,
            player = Player.X,
            winLength = 4
        )

        assertEquals(4, winningCells.size)
    }

    @Test
    fun test11x11WinLength5() {
        val board = Board(11)
        board.set(5, 5, Player.O.toCell())
        board.set(6, 6, Player.O.toCell())
        board.set(7, 7, Player.O.toCell())
        board.set(8, 8, Player.O.toCell())
        board.set(9, 9, Player.O.toCell())

        val winningCells = WinChecker.findWinningCells(
            board = board,
            row = 7,
            column = 7,
            player = Player.O,
            winLength = 5
        )

        assertEquals(5, winningCells.size)
    }

    @Test
    fun testNoWin() {
        val board = Board(3)
        board.set(0, 0, Player.X.toCell())
        board.set(0, 1, Player.X.toCell())

        val winningCells = WinChecker.findWinningCells(
            board = board,
            row = 0,
            column = 1,
            player = Player.X,
            winLength = 3
        )

        assertTrue(winningCells.isEmpty())
    }
}
