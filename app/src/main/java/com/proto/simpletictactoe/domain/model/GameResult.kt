package com.proto.simpletictactoe.domain.model

sealed interface GameResult {
    data object InProgress : GameResult

    data class Winner(
        val player: Player
    ) : GameResult

    data object Draw : GameResult
}
