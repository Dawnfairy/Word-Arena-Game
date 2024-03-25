package com.ft.word_arena_game.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ft.word_arena_game.ui.screens.LoginScreen
import com.ft.word_arena_game.ui.screens.SignUpScreen
import androidx.navigation.compose.rememberNavController
import com.ft.word_arena_game.ui.navigation.Destinations.LoginRoute
import com.ft.word_arena_game.ui.navigation.Destinations.RoomSelectionRoute
import com.ft.word_arena_game.ui.navigation.Destinations.SignUpRoute
import com.ft.word_arena_game.ui.screens.GameSelectionScreen

// NavGraph.kt
object Destinations {
    const val HomeRoute = "home"
    const val LoginRoute = "login"
    const val SignUpRoute = "signup"
    const val RoomSelectionRoute = "roomSelection"

}

@Composable
fun NavGraph(startDestination: String = "login") {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {
        composable(LoginRoute) { LoginScreen(navController) }
        composable(SignUpRoute) { SignUpScreen(navController) }
        composable(RoomSelectionRoute) { GameSelectionScreen(navController) }

        // Add more composable screens here if needed
    }
}
