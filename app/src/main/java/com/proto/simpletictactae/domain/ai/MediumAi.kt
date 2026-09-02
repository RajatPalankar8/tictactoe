package com.proto.simpletictactae.domain.ai

import com.proto.simpletictactae.domain.model.Board
import com.proto.simpletictactae.domain.model.Move
import com.proto.simpletictactae.domain.model.Player
import com.proto.simpletictactae.domain.model.toCell
import kotlin.random.Random

object MediumAi {

    fun findMove(
        board: Board,
        aiPlayer: Player,
        winLength: Int,
        random: Random = Random.Default
    ): Move? {
        // 1. Immediate Win
        val winMove = EasyAi.findImmediateWin(board, aiPlayer, winLength)
        if (winMove != null) return winMove

        // 2. Immediate Block
        val blockMove = EasyAi.findImmediateWin(board, aiPlayer.opponent(), winLength)
        if (blockMove != null) {
            return Move(blockMove.row, blockMove.column, aiPlayer)
        }

        // 3. Evaluate Candidates
        val candidates = CandidateGenerator.generateCandidates(board, radius = 2)
        if (candidates.isEmpty()) return null

        var bestScore = Int.MIN_VALUE
        val bestMoves = mutableListOf<Pair<Int, Int>>()

        for ((r, c) in candidates) {
            val copy = board.copy()
            copy.set(r, c, aiPlayer.toCell())
            val score = BoardEvaluator.evaluate(copy, aiPlayer, winLength)

            if (score > bestScore) {
                bestScore = score
                bestMoves.clear()
                bestMoves.add(r to c)
            } else if (score == bestScore) {
                bestMoves.add(r to c)
            }
        }

        val chosen = bestMoves.randomOrNull(random) ?: candidates.random(random)
        return Move(chosen.first, chosen.second, aiPlayer)
    }
}
