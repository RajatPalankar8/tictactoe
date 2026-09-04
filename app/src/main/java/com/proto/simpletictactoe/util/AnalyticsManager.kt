package com.proto.simpletictactoe.util

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics

object AnalyticsManager {

    private const val TAG = "AnalyticsManager"
    private var firebaseAnalytics: FirebaseAnalytics? = null

    fun initialize(context: Context) {
        try {
            firebaseAnalytics = FirebaseAnalytics.getInstance(context)
            Log.d(TAG, "Firebase Analytics Initialized")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to initialize Firebase Analytics", e)
        }
    }

    fun logGameStarted(gameMode: String, boardSize: String, difficulty: String) {
        val bundle = Bundle().apply {
            putString("game_mode", gameMode)
            putString("board_size", boardSize)
            putString("difficulty", difficulty)
        }
        firebaseAnalytics?.logEvent("game_started", bundle)
    }

    fun logGameEnded(result: String, winningPlayer: String?, gameMode: String) {
        val bundle = Bundle().apply {
            putString("result", result)
            putString("winner", winningPlayer ?: "none")
            putString("game_mode", gameMode)
        }
        firebaseAnalytics?.logEvent("game_ended", bundle)
    }

    fun logScreenView(screenName: String) {
        val bundle = Bundle().apply {
            putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
        }
        firebaseAnalytics?.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
    }
}
