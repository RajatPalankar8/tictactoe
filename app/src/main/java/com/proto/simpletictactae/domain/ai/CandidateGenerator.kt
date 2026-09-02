package com.proto.simpletictactae.domain.ai

import com.proto.simpletictactae.domain.model.Board
import com.proto.simpletictactae.domain.model.Cell
import kotlin.math.abs

object CandidateGenerator {

    fun generateCandidates(board: Board, radius: Int = 2, maxCandidates: Int = 50): List<Pair<Int, Int>> {
        val emptyCells = board.getEmptyCells()
        if (emptyCells.isEmpty()) return emptyList()

        // If board is empty, return center cell
        val occupied = mutableListOf<Pair<Int, Int>>()
        for (r in 0 until board.size) {
            for (c in 0 until board.size) {
                if (board.get(r, c) != Cell.EMPTY) {
                    occupied.add(r to c)
                }
            }
        }

        if (occupied.isEmpty()) {
            val center = board.size / 2
            return listOf(center to center)
        }

        // Find empty cells within radius of occupied cells
        val candidates = emptyCells.filter { (er, ec) ->
            occupied.any { (or, oc) ->
                abs(er - or) <= radius && abs(ec - oc) <= radius
            }
        }.sortedBy { (er, ec) ->
            // Sort by proximity to center and proximity to occupied cells
            val center = board.size / 2.0
            val distToCenter = abs(er - center) + abs(ec - center)
            val minDistToOccupied = occupied.minOf { (or, oc) ->
                maxOf(abs(er - or), abs(ec - oc))
            }
            distToCenter + minDistToOccupied * 2.0
        }

        val result = if (candidates.isNotEmpty()) candidates else emptyCells
        return result.take(maxCandidates)
    }
}
