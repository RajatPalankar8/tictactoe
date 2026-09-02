package com.proto.simpletictactae.domain.ai

import com.proto.simpletictactae.domain.model.Board
import com.proto.simpletictactae.domain.model.Difficulty
import com.proto.simpletictactae.domain.model.Move
import com.proto.simpletictactae.domain.model.Player

interface AiEngine {
    suspend fun findMove(
        board: Board,
        aiPlayer: Player,
        winLength: Int,
        difficulty: Difficulty
    ): Move?
}
