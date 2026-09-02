package com.proto.simpletictactae.domain.model

enum class Cell {
    EMPTY,
    X,
    O
}

fun Player.toCell(): Cell {
    return when (this) {
        Player.X -> Cell.X
        Player.O -> Cell.O
    }
}
