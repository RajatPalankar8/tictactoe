package com.proto.simpletictactae

import com.proto.simpletictactae.domain.ai.AiEngineImpl
import com.proto.simpletictactae.domain.engine.GameEngine
import com.proto.simpletictactae.domain.model.BoardConfigs
import com.proto.simpletictactae.domain.model.Difficulty
import com.proto.simpletictactae.domain.model.Player
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class GameViewModelTest {

    @Test
    fun testAiRespondsAfterHumanMove() = runBlocking {
        val engine = GameEngine(BoardConfigs.CLASSIC, initialPlayer = Player.X)
        val aiEngine = AiEngineImpl()

        // Human plays X at (0, 0)
        engine.makeMove(0, 0)
        assertEquals(Player.O, engine.currentPlayer)

        // AI plays as O
        val aiMove = aiEngine.findMove(
            board = engine.board,
            aiPlayer = Player.O,
            winLength = 3,
            difficulty = Difficulty.EASY
        )

        assertNotNull(aiMove)
        assertNotEquals(0 to 0, aiMove!!.row to aiMove.column)
    }
}
