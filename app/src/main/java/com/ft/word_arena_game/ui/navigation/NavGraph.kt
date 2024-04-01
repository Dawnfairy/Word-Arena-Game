package com.ft.word_arena_game.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ft.word_arena_game.ui.screens.LoginScreen
import com.ft.word_arena_game.ui.screens.SignUpScreen
import androidx.navigation.compose.rememberNavController
import com.ft.word_arena_game.ui.navigation.Destinations.GameRoute
import com.ft.word_arena_game.ui.navigation.Destinations.LoginRoute
import com.ft.word_arena_game.ui.navigation.Destinations.RoomRoute
import com.ft.word_arena_game.ui.navigation.Destinations.RoomSelectionRoute
import com.ft.word_arena_game.ui.navigation.Destinations.RoomTypeSelectionRoute
import com.ft.word_arena_game.ui.navigation.Destinations.SignUpRoute
import com.ft.word_arena_game.ui.screens.GameScreen
import com.ft.word_arena_game.ui.screens.RoomScreen
import com.ft.word_arena_game.ui.screens.RoomSelectionScreen
import com.ft.word_arena_game.ui.screens.RoomTypeSelectionScreen

// NavGraph.kt
object Destinations {
    const val LoginRoute = "login"
    const val SignUpRoute = "signup"
    const val RoomTypeSelectionRoute = "roomTypeSelection"
    const val RoomSelectionRoute = "roomSelection/{roomType}"
    const val RoomRoute = "room/{roomType}/{roomNumber}"
    const val GameRoute = "game"

}

@Composable
fun NavGraph(startDestination: String = "login") {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {
        composable(LoginRoute) { LoginScreen(navController) }
        composable(SignUpRoute) { SignUpScreen(navController) }
        composable(GameRoute) { GameScreen(navController) }


        composable(RoomTypeSelectionRoute) {
            RoomTypeSelectionScreen(navController)
        }
        composable(RoomSelectionRoute) { backStackEntry ->
            val roomType = backStackEntry.arguments?.getString("roomType") ?: return@composable
            RoomSelectionScreen(navController, roomType)
        }
        composable(RoomRoute) { backStackEntry ->
            val roomType = backStackEntry.arguments?.getString("roomType") ?: return@composable
            val roomNumber = backStackEntry.arguments?.getString("roomNumber") ?: return@composable
            RoomScreen(navController, roomType, roomNumber)
        }
    }
}
