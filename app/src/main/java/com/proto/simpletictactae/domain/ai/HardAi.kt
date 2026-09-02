package com.proto.simpletictactae.domain.ai

import com.proto.simpletictactae.domain.engine.WinChecker
import com.proto.simpletictactae.domain.model.Board
import com.proto.simpletictactae.domain.model.Move
import com.proto.simpletictactae.domain.model.Player
import com.proto.simpletictactae.domain.model.toCell

object HardAi {

    fun findMove(
        board: Board,
        aiPlayer: Player,
        winLength: Int
    ): Move? {
        val emptyCells = board.getEmptyCells()
        if (emptyCells.isEmpty()) return null

        // 1. Immediate Win
        val winMove = EasyAi.findImmediateWin(board, aiPlayer, winLength)
        if (winMove != null) return winMove

        // 2. Immediate Block
        val blockMove = EasyAi.findImmediateWin(board, aiPlayer.opponent(), winLength)
        if (blockMove != null) {
            return Move(blockMove.row, blockMove.column, aiPlayer)
        }

        // If 3x3 board, use full Minimax
        if (board.size == 3 && winLength == 3) {
            return minimax3x3(board, aiPlayer)
        }

        // For larger boards (6x6, 9x9, 11x11), use limited-depth minimax with candidates
        return alphaBetaSearch(board, aiPlayer, winLength, searchDepth = 2)
    }

    private fun minimax3x3(board: Board, aiPlayer: Player): Move? {
        var bestScore = Int.MIN_VALUE
        var bestMove: Move? = null

        val emptyCells = board.getEmptyCells()
        for ((r, c) in emptyCells) {
            val copy = board.copy()
            copy.set(r, c, aiPlayer.toCell())

            val score = minimax(
                board = copy,
                depth = 0,
                isMaximizing = false,
                aiPlayer = aiPlayer,
                lastR = r,
                lastC = c,
                lastPlayer = aiPlayer,
                winLength = 3,
                alpha = Int.MIN_VALUE,
                beta = Int.MAX_VALUE
            )

            if (score > bestScore) {
                bestScore = score
                bestMove = Move(r, c, aiPlayer)
            }
        }

        return bestMove
    }

    private fun minimax(
        board: Board,
        depth: Int,
        isMaximizing: Boolean,
        aiPlayer: Player,
        lastR: Int,
        lastC: Int,
        lastPlayer: Player,
        winLength: Int,
        alpha: Int,
        beta: Int
    ): Int {
        val winningCells = WinChecker.findWinningCells(board, lastR, lastC, lastPlayer, winLength)
        if (winningCells.isNotEmpty()) {
            return if (lastPlayer == aiPlayer) 100 - depth else -100 + depth
        }

        if (board.isFull()) return 0

        var currentAlpha = alpha
        var currentBeta = beta

        if (isMaximizing) {
            var maxEval = Int.MIN_VALUE
            for ((r, c) in board.getEmptyCells()) {
                val copy = board.copy()
                copy.set(r, c, aiPlayer.toCell())
                val eval = minimax(copy, depth + 1, false, aiPlayer, r, c, aiPlayer, winLength, currentAlpha, currentBeta)
                maxEval = maxOf(maxEval, eval)
                currentAlpha = maxOf(currentAlpha, eval)
                if (currentBeta <= currentAlpha) break
            }
            return maxEval
        } else {
            var minEval = Int.MAX_VALUE
            val opp = aiPlayer.opponent()
            for ((r, c) in board.getEmptyCells()) {
                val copy = board.copy()
                copy.set(r, c, opp.toCell())
                val eval = minimax(copy, depth + 1, true, aiPlayer, r, c, opp, winLength, currentAlpha, currentBeta)
                minEval = minOf(minEval, eval)
                currentBeta = minOf(currentBeta, eval)
                if (currentBeta <= currentAlpha) break
            }
            return minEval
        }
    }

    private fun alphaBetaSearch(
        board: Board,
        aiPlayer: Player,
        winLength: Int,
        searchDepth: Int
    ): Move? {
        val candidates = CandidateGenerator.generateCandidates(board, radius = 2)
        if (candidates.isEmpty()) return null

        var bestScore = Int.MIN_VALUE
        var bestMove: Pair<Int, Int>? = null

        for ((r, c) in candidates) {
            val copy = board.copy()
            copy.set(r, c, aiPlayer.toCell())

            val score = limitedSearch(
                board = copy,
                depth = 1,
                maxDepth = searchDepth,
                isMaximizing = false,
                aiPlayer = aiPlayer,
                winLength = winLength,
                alpha = Int.MIN_VALUE,
                beta = Int.MAX_VALUE
            )

            if (score > bestScore || bestMove == null) {
                bestScore = score
                bestMove = r to c
            }
        }

        return (bestMove ?: candidates.firstOrNull())?.let { (r, c) -> Move(r, c, aiPlayer) }
    }

    private fun limitedSearch(
        board: Board,
        depth: Int,
        maxDepth: Int,
        isMaximizing: Boolean,
        aiPlayer: Player,
        winLength: Int,
        alpha: Int,
        beta: Int
    ): Int {
        if (depth >= maxDepth || board.isFull()) {
            return BoardEvaluator.evaluate(board, aiPlayer, winLength)
        }

        val candidates = CandidateGenerator.generateCandidates(board, radius = 2)
        if (candidates.isEmpty()) {
            return BoardEvaluator.evaluate(board, aiPlayer, winLength)
        }

        var currentAlpha = alpha
        var currentBeta = beta

        if (isMaximizing) {
            var maxEval = Int.MIN_VALUE
            for ((r, c) in candidates) {
                val copy = board.copy()
                copy.set(r, c, aiPlayer.toCell())
                val eval = limitedSearch(copy, depth + 1, maxDepth, false, aiPlayer, winLength, currentAlpha, currentBeta)
                maxEval = maxOf(maxEval, eval)
                currentAlpha = maxOf(currentAlpha, eval)
                if (currentBeta <= currentAlpha) break
            }
            return maxEval
        } else {
            var minEval = Int.MAX_VALUE
            val opp = aiPlayer.opponent()
            for ((r, c) in candidates) {
                val copy = board.copy()
                copy.set(r, c, opp.toCell())
                val eval = limitedSearch(copy, depth + 1, maxDepth, true, aiPlayer, winLength, currentAlpha, currentBeta)
                minEval = minOf(minEval, eval)
                currentBeta = minOf(currentBeta, eval)
                if (currentBeta <= currentAlpha) break
            }
            return minEval
        }
    }
}
