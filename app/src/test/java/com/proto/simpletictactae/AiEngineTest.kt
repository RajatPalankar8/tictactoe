package com.proto.simpletictactae

import com.proto.simpletictactae.domain.ai.HardAi
import com.proto.simpletictactae.domain.ai.MediumAi
import com.proto.simpletictactae.domain.model.Board
import com.proto.simpletictactae.domain.model.Player
import com.proto.simpletictactae.domain.model.toCell
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class AiEngineTest {

    @Test
    fun testHardAiTakesImmediateWin() {
        val board = Board(3)
        // X X _
        // O O _
        // _ _ _
        board.set(0, 0, Player.X.toCell())
        board.set(0, 1, Player.X.toCell())
        board.set(1, 0, Player.O.toCell())
        board.set(1, 1, Player.O.toCell())

        val move = HardAi.findMove(board, Player.X, winLength = 3)

        assertNotNull(move)
        assertEquals(0, move?.row)
        assertEquals(2, move?.column)
    }

    @Test
    fun testHardAiBlocksImmediateLoss() {
        val board = Board(3)
        // O O _
        // X _ _
        // _ _ _
        board.set(0, 0, Player.O.toCell())
        board.set(0, 1, Player.O.toCell())
        board.set(1, 0, Player.X.toCell())

        val move = HardAi.findMove(board, Player.X, winLength = 3)

        assertNotNull(move)
        assertEquals(0, move?.row)
        assertEquals(2, move?.column)
    }

    @Test
    fun testMediumAiFindsMove() {
        val board = Board(6)
        board.set(2, 2, Player.X.toCell())

        val move = MediumAi.findMove(board, Player.O, winLength = 4)

        assertNotNull(move)
    }
}
