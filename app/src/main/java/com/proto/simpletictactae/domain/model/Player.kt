package com.proto.simpletictactae.domain.model

enum class Player {
    X,
    O;

    fun opponent(): Player {
        return if (this == X) O else X
    }
}
