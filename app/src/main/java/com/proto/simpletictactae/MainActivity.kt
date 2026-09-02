package com.proto.simpletictactae

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.proto.simpletictactae.ui.navigation.AppNavigation
import com.proto.simpletictactae.ui.theme.SimpleTicTacTaeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SimpleTicTacTaeTheme {
                AppNavigation()
            }
        }
    }
}
