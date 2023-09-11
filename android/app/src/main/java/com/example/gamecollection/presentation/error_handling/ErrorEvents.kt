package com.example.gamecollection.presentation.error_handling

import com.example.gamecollection.presentation.add_update.AddGameViewModel

sealed class ErrorEvents () {
    object ErrorOnFetchingData: ErrorEvents()
    object ErrorOnAddingGame: ErrorEvents()
    object ErrorOnDeletingGame: ErrorEvents()
    object ErrorOnUpdatingGame: ErrorEvents()
}
