package com.example.gamecollection.presentation.game_details

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamecollection.GameLibraryApplication
import com.example.gamecollection.di.ApiRepository
import com.example.gamecollection.di.DaoRepository
import com.example.gamecollection.domain.model.Game
import com.example.gamecollection.domain.repository.GameRepository
import com.example.gamecollection.presentation.add_update.AddGameViewModel
import com.example.gamecollection.presentation.error_handling.ErrorEvents
import com.example.gamecollection.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameDetailsViewModel @Inject constructor(
    @DaoRepository private val repository: GameRepository,
    @ApiRepository private val apiRepository: GameRepository,
    private val savedStateHandle: SavedStateHandle,
    app: Application
): AndroidViewModel(app){
    private val _gameState = mutableStateOf(GameDetailsState())
    val gameState: State<GameDetailsState> = _gameState

    init {
        initializeGame()
    }

    private fun initializeGame() {
        val gameId = savedStateHandle.get<Int>("selectedGameId")!!
        if (gameId != -1) {
            if (hasInternetConnection()) {
                viewModelScope.launch {
                    apiRepository.getGameById(gameId).collect { result ->
                        when (result) {
                            is Resource.Success -> {
                                _gameState.value = gameState.value.copy(game = result.data)                    }
                            is Resource.Failure -> {

                            }
                        }
                    }
                }
            }
            else {
                viewModelScope.launch {
                    repository.getGameById(gameId).collect { result ->
                        when (result) {
                            is Resource.Success -> {
                                if (result.data != null) {
                                    _gameState.value = gameState.value.copy(game = result.data)
                                }
                            }
                            is Resource.Failure -> {
                            }
                            is Resource.Loading -> {
                            }
                        }
                    }
                }
            }
        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<GameLibraryApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}