package com.example.gamecollection.domain.use_case

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.gamecollection.di.ApiRepository
import com.example.gamecollection.di.DaoRepository
import com.example.gamecollection.domain.model.Game
import com.example.gamecollection.domain.repository.GameRepository
import com.example.gamecollection.util.Resource
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class SyncronizeData @Inject constructor(
    @DaoRepository private val repository: GameRepository,
    @ApiRepository private val apiRepository: GameRepository
){

    suspend fun syncServer(apiGames: MutableList<Game>, dbGames: List<Game>): List<Game> {
        for (game: Game in dbGames) {
            if (game.syncFlag != "UpToDate") {
                when (game.syncFlag) {
                    "Add" -> {
                        apiRepository.addGameToLibrary(game).collect { result ->
                            when (result) {
                                is Resource.Success -> {
                                    if (game.id != null && result.data != null) {
                                        repository.hardDeleteGame(game.id).collect()
                                        repository.addGameToLibrary(result.data).collect()
                                        apiGames.add(result.data)
                                    }
                                }
                            }
                        }
                    }
                    "Update" -> {
                        apiRepository.updateGame(game).collect{ result ->
                            when (result) {
                                is Resource.Success -> {
                                    if (game.id != null && result.data != null) {
                                        apiGames.forEachIndexed { index, gameApi ->
                                            gameApi.takeIf { it.id == game.id}?.let {
                                                apiGames[index] = game
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    "Delete" -> {
                        game.id?.let { apiRepository.hardDeleteGame(it).collect { result ->
                            when (result) {
                                is Resource.Success -> {
                                    if (game.id != null && result.data != null) {
                                        apiGames.removeIf { game -> game.id == result.data.id }
                                    }
                                }
                            }
                        } }
                    }
                }
                repository.updateGame(game, "UpToDate").collect()
            }
        }
        return apiGames
    }

    suspend fun syncDb(apiGames: List<Game>, dbGames: List<Game>) {
        for (game: Game in apiGames) {
            val dbGame = dbGames.find{ gameDb -> game.id == gameDb.id }
            if (dbGame == null) {
                repository.addGameToLibrary(game, "UpToDate").collect()
            }
            else {
                if (dbGame != game) {
                    repository.updateGame(game, "UpToDate").collect()
                }
            }
        }
        for (game: Game in dbGames) {
            val apiGame = apiGames.find{ gameApi -> game.id == gameApi.id }
            if (apiGame == null) {
                game.id?.let { repository.hardDeleteGame(it).collect() }
            }
        }
    }
}