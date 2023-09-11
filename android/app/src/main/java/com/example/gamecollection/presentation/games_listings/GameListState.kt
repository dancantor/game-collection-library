package com.example.gamecollection.presentation.games_listings

import com.example.gamecollection.domain.model.Game

data class GameListState (
    val games: List<Game> = emptyList(),
    val isLoading: Boolean = false
) {
}