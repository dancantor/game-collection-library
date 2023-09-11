package com.example.gamecollection.presentation.games_listings

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamecollection.GameLibraryApplication
import com.example.gamecollection.di.ApiRepository
import com.example.gamecollection.di.DaoRepository
import com.example.gamecollection.domain.model.Game
import com.example.gamecollection.domain.repository.GameRepository
import com.example.gamecollection.domain.use_case.SyncronizeData
import com.example.gamecollection.presentation.add_update.AddGameViewModel
import com.example.gamecollection.presentation.error_handling.ErrorEvents
import com.example.gamecollection.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameListViewModel @Inject constructor(
    @DaoRepository private val repository: GameRepository,
    @ApiRepository private val apiRepository: GameRepository,
    private val syncronizeData: SyncronizeData,
    app: Application
): AndroidViewModel(app) {
    private val _gamesState = mutableStateOf(GameListState())
    val gamesState: State<GameListState> = _gamesState

    private val errorEventChannel = Channel<ErrorEvents> {  }
    val errorEvents = errorEventChannel.receiveAsFlow()

    private var getGamesJob: Job? = null

    init {
        getGamesList()
    }

    private fun getGamesList() {
//        viewModelScope.launch {
//            repository.getGamesListings().collect { resultDb ->
//                when (resultDb) {
//                    is Resource.Success -> {
//                        if (resultDb.data != null) {
//                        _gamesState.value = gamesState.value.copy(games = resultDb.data)
//                        }
//                    }
//                    is Resource.Failure -> {
//
//                    }
//                    is Resource.Loading -> {
//
//                    }
//                }
//            }
//        }
        if (hasInternetConnection()) {
            viewModelScope.launch {
                var launchCall: Boolean = true;
                apiRepository.getGamesListings().collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            if (result.data != null) {
                                repository.getGamesListings().collect { resultDb ->
                                    when (resultDb) {
                                        is Resource.Success -> {
                                            if (launchCall && (resultDb.data != null)) {
                                                val syncronisedGames: List<Game>  = syncronizeData.syncServer(
                                                    result.data.toMutableList(),
                                                    resultDb.data
                                                )
                                                syncronizeData.syncDb(syncronisedGames, resultDb.data)
                                                launchCall = false
                                            }
                                            if (resultDb.data != null) {
                                                _gamesState.value =
                                                    gamesState.value.copy(games =
                                                    resultDb.data.filter { game ->
                                                        game.syncFlag != "Delete"
                                                    })
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
                        is Resource.Failure -> {
                            errorEventChannel.send(ErrorEvents.ErrorOnFetchingData)
                        }
                        is Resource.Loading -> {
                        }
                    }
                }
            }

        } else {
            viewModelScope.launch {
                repository.getGamesListings().collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            if (result.data != null) {
                                _gamesState.value = gamesState.value.copy(games =
                                    result.data.filter { game ->
                                        game.syncFlag != "Delete"
                                    })
                            }
                        }
                        is Resource.Failure -> {
                            errorEventChannel.send(ErrorEvents.ErrorOnFetchingData)
                        }
                        is Resource.Loading -> {
                        }
                    }
                }
            }
        }
    }

    public fun deleteGame(gameId: Int?) {
        if (gameId == null) return
        if (hasInternetConnection()) {
            viewModelScope.launch {
                apiRepository.hardDeleteGame(gameId).collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            repository.hardDeleteGame(gameId).collect {
                            }
                        }
                        is Resource.Failure -> {
                            errorEventChannel.send(ErrorEvents.ErrorOnDeletingGame)
                        }
                        is Resource.Loading -> {
                        }
                    }
                }
            }
        }
        else {
                viewModelScope.launch {
                    repository.softDeleteGame(gameId, 0.0).collect { result ->
                        when (result) {
                            is Resource.Success -> {
                            }
                            is Resource.Failure -> {
                                errorEventChannel.send(ErrorEvents.ErrorOnDeletingGame)
                            }
                            is Resource.Loading -> {
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
            capabilities.hasTransport(TRANSPORT_WIFI) -> true
            capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}