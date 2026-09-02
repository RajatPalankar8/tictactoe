package com.proto.simpletictactae.domain.model

sealed interface MoveResult {
    data object Invalid : MoveResult

    data class Continue(
        val nextPlayer: Player
    ) : MoveResult

    data class Win(
        val player: Player,
        val cells: List<Pair<Int, Int>>
    ) : MoveResult

    data object Draw : MoveResult
}
