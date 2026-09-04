package com.proto.simpletictactoe.domain.ai

import com.proto.simpletictactoe.domain.model.Board
import com.proto.simpletictactoe.domain.model.Difficulty
import com.proto.simpletictactoe.domain.model.Move
import com.proto.simpletictactoe.domain.model.Player

interface AiEngine {
    suspend fun findMove(
        board: Board,
        aiPlayer: Player,
        winLength: Int,
        difficulty: Difficulty
    ): Move?
}
