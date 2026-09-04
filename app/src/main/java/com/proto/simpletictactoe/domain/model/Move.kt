package com.proto.simpletictactoe.domain.model

data class Move(
    val row: Int,
    val column: Int,
    val player: Player
)
