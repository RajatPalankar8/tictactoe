package com.proto.simpletictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.proto.simpletictactoe.ui.navigation.AppNavigation
import com.proto.simpletictactoe.ui.theme.SimpleTicTacTaeTheme
import com.proto.simpletictactoe.util.AdMobManager
import com.proto.simpletictactoe.util.AnalyticsManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize AdMob & Firebase Analytics
        AdMobManager.initialize(this)
        AdMobManager.loadInterstitialAd(this)
        AnalyticsManager.initialize(this)

        setContent {
            SimpleTicTacTaeTheme {
                AppNavigation()
            }
        }
    }
}
