package com.example.gamecollection.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gamecollection.presentation.add_update.AddGameScreen
import com.example.gamecollection.presentation.game_details.GameDetailsScreen
import com.example.gamecollection.presentation.game_details.GameDetailsViewModel
import com.example.gamecollection.presentation.games_listings.GameListScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.MainScreen.route) {
        composable(route = Screen.MainScreen.route) {
            GameListScreen(
                navController,
                onGameClick = {gameId: Int? ->
                    navController.navigate("${Screen.DetailsScreen.route}/${gameId}")
                }
            )
        }
        composable(
            route = "${Screen.DetailsScreen.route}/{selectedGameId}",
            arguments = listOf(
                navArgument("selectedGameId") {type = NavType.IntType}
            )
        ) { navBackStackEntry ->
            navBackStackEntry.arguments?.getInt("selectedGameId")?.let { it
                GameDetailsScreen(
                    navController = navController
                )

            }
        }
        composable(
            route = "${Screen.AddScreen.route}"
        ) {
           AddGameScreen(
               navController = navController,
           )
        }

        composable(
            route = "${Screen.UpdateScreen.route}/{gameId}",
            arguments = listOf(
                navArgument("gameId") {type = NavType.IntType}
            )
        ) {
            AddGameScreen(
                navController = navController,
            )
        }
    }
}