package com.ft.word_arena_game.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ft.word_arena_game.ui.screens.LoginScreen
import com.ft.word_arena_game.ui.screens.SignUpScreen
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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
    const val GameRoute = "game/{roomType}/{roomNumber}/{randomLetter}/{wordIndex}/{rivalId}"
}

@Composable
fun NavGraph(startDestination: String = "login") {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {
        composable(LoginRoute) { LoginScreen(navController) }
        composable(SignUpRoute) { SignUpScreen(navController) }

        composable(RoomTypeSelectionRoute) {
            RoomTypeSelectionScreen(navController)
        }
        composable(
            route = RoomSelectionRoute,
            arguments = listOf(
                navArgument("roomType") { type = NavType.StringType })
        ) { backStackEntry ->
            val roomType = backStackEntry.arguments?.getString("roomType")?.toBoolean() ?: false
            RoomSelectionScreen(navController, roomType)
        }


        composable(route = RoomRoute,
            arguments = listOf(
                navArgument("roomType") { type = NavType.BoolType },
                navArgument("roomNumber") { type = NavType.StringType }
            )) { backStackEntry ->
            val roomType = backStackEntry.arguments?.getBoolean("roomType") ?: false
            val roomNumber = backStackEntry.arguments?.getString("roomNumber") ?: ""
            RoomScreen(navController, roomType, roomNumber)
        }

        composable(route = GameRoute,
            arguments = listOf(
                navArgument("roomType") { type = NavType.BoolType },
                navArgument("roomNumber") { type = NavType.StringType },
                navArgument("randomLetter") { type = NavType.StringType },
                navArgument("wordIndex") { type = NavType.IntType },
                navArgument("rivalId") { type = NavType.StringType },
            )) { backStackEntry ->
            val roomType = backStackEntry.arguments?.getBoolean("roomType") ?: false
            val roomNumber = backStackEntry.arguments?.getString("roomNumber") ?: ""
            val randomLetter = backStackEntry.arguments?.getString("randomLetter") ?: ""
            val wordIndex = backStackEntry.arguments?.getInt("wordIndex") ?: -1
            val rivalId = backStackEntry.arguments?.getString("rivalId") ?: ""
            GameScreen(navController, roomType, roomNumber, randomLetter, wordIndex, rivalId)
        }
    }
}
