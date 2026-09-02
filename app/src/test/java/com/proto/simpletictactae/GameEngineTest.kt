package com.proto.simpletictactae

import com.proto.simpletictactae.domain.engine.GameEngine
import com.proto.simpletictactae.domain.model.BoardConfigs
import com.proto.simpletictactae.domain.model.Cell
import com.proto.simpletictactae.domain.model.MoveResult
import com.proto.simpletictactae.domain.model.Player
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GameEngineTest {

    @Test
    fun testInitialPlayer() {
        val engine = GameEngine(BoardConfigs.CLASSIC)
        assertEquals(Player.X, engine.currentPlayer)
    }

    @Test
    fun testValidMoveAndTurnSwitch() {
        val engine = GameEngine(BoardConfigs.CLASSIC)
        val result = engine.makeMove(0, 0)

        assertTrue(result is MoveResult.Continue)
        assertEquals(Player.O, (result as MoveResult.Continue).nextPlayer)
        assertEquals(Cell.X, engine.board.get(0, 0))
    }

    @Test
    fun testOccupiedCellInvalidMove() {
        val engine = GameEngine(BoardConfigs.CLASSIC)
        engine.makeMove(0, 0)
        val secondResult = engine.makeMove(0, 0)

        assertTrue(secondResult is MoveResult.Invalid)
    }

    @Test
    fun testOutOfBoundsMove() {
        val engine = GameEngine(BoardConfigs.CLASSIC)
        val result = engine.makeMove(-1, 5)

        assertTrue(result is MoveResult.Invalid)
    }

    @Test
    fun testWinCondition() {
        val engine = GameEngine(BoardConfigs.CLASSIC)
        engine.makeMove(0, 0) // X
        engine.makeMove(1, 0) // O
        engine.makeMove(0, 1) // X
        engine.makeMove(1, 1) // O
        val result = engine.makeMove(0, 2) // X wins

        assertTrue(result is MoveResult.Win)
        assertEquals(Player.X, (result as MoveResult.Win).player)
        assertEquals(3, result.cells.size)
    }

    @Test
    fun testDrawCondition() {
        val engine = GameEngine(BoardConfigs.CLASSIC)
        // X O X
        // X X O
        // O X O
        engine.makeMove(0, 0) // X
        engine.makeMove(0, 1) // O
        engine.makeMove(0, 2) // X
        engine.makeMove(1, 2) // O
        engine.makeMove(1, 0) // X
        engine.makeMove(2, 0) // O
        engine.makeMove(1, 1) // X
        engine.makeMove(2, 2) // O
        val result = engine.makeMove(2, 1) // X draw

        assertTrue(result is MoveResult.Draw)
    }
}
