package com.example.gamecollection.presentation

import com.example.gamecollection.domain.model.Game
import com.example.gamecollection.presentation.games_listings.GameListViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

class EventFlow @Inject constructor() {
    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    sealed class UiEvent(game: Game? = null, gameId: Int? = null) {
        class SaveGame(game: Game): UiEvent()
    }
}