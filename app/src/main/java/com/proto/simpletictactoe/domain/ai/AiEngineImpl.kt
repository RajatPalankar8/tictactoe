package com.proto.simpletictactoe.domain.ai

import com.proto.simpletictactoe.domain.model.Board
import com.proto.simpletictactoe.domain.model.Difficulty
import com.proto.simpletictactoe.domain.model.Move
import com.proto.simpletictactoe.domain.model.Player
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AiEngineImpl : AiEngine {

    override suspend fun findMove(
        board: Board,
        aiPlayer: Player,
        winLength: Int,
        difficulty: Difficulty
    ): Move? = withContext(Dispatchers.Default) {
        when (difficulty) {
            Difficulty.EASY -> EasyAi.findMove(board, aiPlayer, winLength)
            Difficulty.MEDIUM -> MediumAi.findMove(board, aiPlayer, winLength)
            Difficulty.HARD -> HardAi.findMove(board, aiPlayer, winLength)
        }
    }
}
