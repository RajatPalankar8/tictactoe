package com.proto.simpletictactoe.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.proto.simpletictactoe.domain.model.Player
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StatisticsRepository(private val context: Context) {

    private object Keys {
        val GAMES_PLAYED = intPreferencesKey("stat_games_played")
        val WINS = intPreferencesKey("stat_wins")
        val LOSSES = intPreferencesKey("stat_losses")
        val DRAWS = intPreferencesKey("stat_draws")
        val X_WINS = intPreferencesKey("stat_x_wins")
        val O_WINS = intPreferencesKey("stat_o_wins")
        val CLASSIC_GAMES = intPreferencesKey("stat_classic_games")
        val MEGA_GAMES = intPreferencesKey("stat_mega_games")
        val PRO_GAMES = intPreferencesKey("stat_pro_games")
        val ULTIMATE_GAMES = intPreferencesKey("stat_ultimate_games")
    }

    val statisticsFlow: Flow<Statistics> = context.dataStore.data.map { prefs ->
        Statistics(
            gamesPlayed = prefs[Keys.GAMES_PLAYED] ?: 0,
            wins = prefs[Keys.WINS] ?: 0,
            losses = prefs[Keys.LOSSES] ?: 0,
            draws = prefs[Keys.DRAWS] ?: 0,
            xWins = prefs[Keys.X_WINS] ?: 0,
            oWins = prefs[Keys.O_WINS] ?: 0,
            classicGames = prefs[Keys.CLASSIC_GAMES] ?: 0,
            megaGames = prefs[Keys.MEGA_GAMES] ?: 0,
            proGames = prefs[Keys.PRO_GAMES] ?: 0,
            ultimateGames = prefs[Keys.ULTIMATE_GAMES] ?: 0
        )
    }

    suspend fun recordWin(winner: Player, boardSize: Int, againstComputer: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[Keys.GAMES_PLAYED] = (prefs[Keys.GAMES_PLAYED] ?: 0) + 1
            if (againstComputer) {
                prefs[Keys.WINS] = (prefs[Keys.WINS] ?: 0) + 1
            }
            if (winner == Player.X) {
                prefs[Keys.X_WINS] = (prefs[Keys.X_WINS] ?: 0) + 1
            } else {
                prefs[Keys.O_WINS] = (prefs[Keys.O_WINS] ?: 0) + 1
            }
            incrementBoardGames(prefs, boardSize)
        }
    }

    suspend fun recordLoss(boardSize: Int) {
        context.dataStore.edit { prefs ->
            prefs[Keys.GAMES_PLAYED] = (prefs[Keys.GAMES_PLAYED] ?: 0) + 1
            prefs[Keys.LOSSES] = (prefs[Keys.LOSSES] ?: 0) + 1
            incrementBoardGames(prefs, boardSize)
        }
    }

    suspend fun recordDraw(boardSize: Int) {
        context.dataStore.edit { prefs ->
            prefs[Keys.GAMES_PLAYED] = (prefs[Keys.GAMES_PLAYED] ?: 0) + 1
            prefs[Keys.DRAWS] = (prefs[Keys.DRAWS] ?: 0) + 1
            incrementBoardGames(prefs, boardSize)
        }
    }

    suspend fun resetStatistics() {
        context.dataStore.edit { prefs ->
            prefs.clear()
        }
    }

    private fun incrementBoardGames(prefs: androidx.datastore.preferences.core.MutablePreferences, boardSize: Int) {
        when (boardSize) {
            3 -> prefs[Keys.CLASSIC_GAMES] = (prefs[Keys.CLASSIC_GAMES] ?: 0) + 1
            6 -> prefs[Keys.MEGA_GAMES] = (prefs[Keys.MEGA_GAMES] ?: 0) + 1
            9 -> prefs[Keys.PRO_GAMES] = (prefs[Keys.PRO_GAMES] ?: 0) + 1
            11 -> prefs[Keys.ULTIMATE_GAMES] = (prefs[Keys.ULTIMATE_GAMES] ?: 0) + 1
        }
    }
}
