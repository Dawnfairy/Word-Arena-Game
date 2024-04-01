package com.ft.word_arena_game

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.ft.word_arena_game.ui.navigation.NavGraph
import com.ft.word_arena_game.ui.theme.WordArenaGameTheme
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {
            WordArenaGameTheme {

                NavGraph()


            }
        }
    }
}