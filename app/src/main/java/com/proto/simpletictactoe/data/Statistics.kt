package com.proto.simpletictactoe.data

data class Statistics(
    val gamesPlayed: Int = 0,
    val wins: Int = 0,
    val losses: Int = 0,
    val draws: Int = 0,
    val xWins: Int = 0,
    val oWins: Int = 0,
    val classicGames: Int = 0,
    val megaGames: Int = 0,
    val proGames: Int = 0,
    val ultimateGames: Int = 0
)
