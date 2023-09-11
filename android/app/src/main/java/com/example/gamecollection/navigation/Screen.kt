package com.example.gamecollection.navigation

sealed class Screen(val route: String) {
    object MainScreen: Screen("games-library")
    object DetailsScreen : Screen("game-detail}")
    object AddScreen: Screen("add-game")
    object UpdateScreen: Screen("update-game")
}
