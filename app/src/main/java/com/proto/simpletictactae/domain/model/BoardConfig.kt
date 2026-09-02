package com.proto.simpletictactae.domain.model

data class BoardConfig(
    val size: Int,
    val winLength: Int
)

object BoardConfigs {
    val CLASSIC = BoardConfig(3, 3)
    val MEGA = BoardConfig(6, 4)
    val PRO = BoardConfig(9, 5)
    val ULTIMATE = BoardConfig(11, 5)

    val all = listOf(
        CLASSIC,
        MEGA,
        PRO,
        ULTIMATE
    )
}
