package com.example.gamecollection.data.repository

import android.util.Log
import com.example.gamecollection.data.mapper.toGame
import com.example.gamecollection.data.mapper.toGameDto
import com.example.gamecollection.data.remote.api.RetrofitInstance
import com.example.gamecollection.domain.model.Game
import com.example.gamecollection.domain.repository.GameRepository
import com.example.gamecollection.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameRepositoryApiImpl  @Inject constructor(): GameRepository {
    override fun getGamesListings(): Flow<Resource<List<Game>>> {
        return flow {
            Log.i("getGamesListingsApi", "entered")
            emit(Resource.Loading(true))
            val gamesFromApi = RetrofitInstance.api.getGames()
            if (!gamesFromApi.isSuccessful) {
                emit(Resource.Failure("Error on listing games from api"));
            }
            else {
                if (gamesFromApi.body() != null) {
                    Log.i("getGamesListingsApi", "successfull, games=${gamesFromApi.body()}")
                    emit(Resource.Success(data= gamesFromApi.body()!!.map { game -> game.toGame() }))
                }
            }
            emit(Resource.Loading(false))
            Log.i("getGamesListingsApi", "exited")
            return@flow
        }
    }

    override suspend fun getGameById(gameId: Int): Flow<Resource<Game>> {
        return flow {
            Log.i("getGameByIdApi", "entered, gameId=${gameId}")
            emit(Resource.Loading(true))
            val game = RetrofitInstance.api.getGameById(gameId);
            if (game.code() == 404) {
                Log.i("getGameByIdApi", "not found")
                emit(Resource.Failure("Game with id $gameId was not found"));
            }
            else if (!game.isSuccessful) {
                Log.i("getGameByIdApi", "Error on retrieving, message=${game.message()}")
                emit(Resource.Failure("Error on getting game with id $gameId"));
            }
            else {
                if (game.body() != null) {
                    Log.i("getGameByIdApi", "successfull, game=${game.body()}")
                    emit(Resource.Success(data= game.body()!!.toGame()))
                }
            }
            emit(Resource.Loading(false))
            Log.i("getGameByIdApi", "exited")
            return@flow
        }
    }

    override suspend fun addGameToLibrary(game: Game, syncStatus: String): Flow<Resource<Game>> {
        return flow {
            Log.i("addGameToLibraryApi", "entered, game=$game")
            emit(Resource.Loading(true))
            val gameResponse = RetrofitInstance.api.addNewGame(game.toGameDto());
            if (!gameResponse.isSuccessful) {
                Log.i("getGameByIdApi", "Error on adding, message=${gameResponse.message()}")
                emit(Resource.Failure("Error on adding the game"));
            }
            else {
                if (gameResponse.body() != null) {
                    Log.i("addGameToLibraryApi", "successfully, game=$game")
                    emit(Resource.Success(data= gameResponse.body()!!.toGame()))
                }
            }
            emit(Resource.Loading(false))
            Log.i("addGameToLibraryApi", "successfully")
            return@flow
        }
    }

    override suspend fun softDeleteGame(gameId: Int, sellPrice: Double): Flow<Resource<Game>> {
        // This is useless in case of Api, for the current moment
        // That's why I've copy pasted getById
        return flow {
            emit(Resource.Loading(true))
            val game = RetrofitInstance.api.getGameById(gameId);
            if (game.code() == 404) {
                emit(Resource.Failure("Game with id $gameId was not found"));
            }
            else if (!game.isSuccessful) {
                emit(Resource.Failure("Error on getting game with id $gameId"));
            }
            else {
                if (game.body() != null) {
                    emit(Resource.Success(data= game.body()!!.toGame()))
                }
            }
            emit(Resource.Loading(false))
            return@flow
        }
    }

    override suspend fun hardDeleteGame(gameId: Int): Flow<Resource<Game>> {
        Log.i("hardDeleteGame", "entered, gameId=$gameId")
        return flow {
            emit(Resource.Loading(true))
            val game = RetrofitInstance.api.deleteGame(gameId);
            if (game.code() == 404) {
                Log.i("hardDeleteGame", "not found")
                emit(Resource.Failure("Game with id $gameId was not found"));
            }
            else if (!game.isSuccessful) {
                Log.i("hardDeleteGame", "Error on deleting, message=${game.message()}")
                emit(Resource.Failure("Error on deleting game with id $gameId"));
            }
            else {
                if (game.body() != null) {
                    Log.i("hardDeleteGame", "successfully, game=${game.body()}")
                    emit(Resource.Success(data= game.body()!!.toGame()))
                }
            }
            Log.i("hardDeleteGame", "successfully")
            emit(Resource.Loading(false))
            return@flow
        }
    }

    override suspend fun updateGame(game: Game, syncStatus: String): Flow<Resource<Game>> {
        return flow {
            Log.i("updateGame", "entered, game=${game.id}")
            emit(Resource.Loading(true))
            if (game.id != null) {
                val gameResponse = RetrofitInstance.api.updateGame(game.toGameDto(), game.id);
                if (!gameResponse.isSuccessful) {
                    Log.i("updateGame", "successfull, game=$game")
                    emit(Resource.Failure("Error on updating the game with id ${game.id}"));
                }
                else {
                    if (gameResponse.body() != null) {
                        emit(Resource.Success(data= gameResponse.body()!!.toGame()))
                    }
                }
            }
            emit(Resource.Loading(false))
            Log.i("updateGame", "successfully")

            return@flow
        }
    }
}