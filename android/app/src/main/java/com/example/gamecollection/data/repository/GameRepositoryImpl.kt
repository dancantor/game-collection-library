package com.example.gamecollection.data.repository

import android.util.Log
import com.example.gamecollection.data.local.GameEntity
import com.example.gamecollection.data.local.GamesDatabase
import com.example.gamecollection.data.mapper.toGame
import com.example.gamecollection.data.mapper.toGameEntity
import com.example.gamecollection.domain.model.Game
import com.example.gamecollection.domain.repository.GameRepository
import com.example.gamecollection.util.Resource
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameRepositoryImpl  @Inject constructor(private val gamesDatabase: GamesDatabase): GameRepository {
    private val gamesDao = gamesDatabase.gameDao


    override fun getGamesListings(): Flow<Resource<List<Game>>> {
        return flow {
            emit(Resource.Loading(true))
            gamesDao.getAllGames().map { games -> games.map { game -> game.toGame() } } .collect { result ->
                emit(Resource.Success(data = result))
            }
            emit(Resource.Loading(false))
            return@flow
        }
    }

    override suspend fun getGameById(
        gameId: Int
    ): Flow<Resource<Game>> {
        return flow {
            emit(Resource.Loading(true))
            emit(Resource.Success(
                data = gamesDao.getGameById(gameId).toGame()
            ))
            emit(Resource.Loading(false))
            return@flow
        }
    }

    override suspend fun addGameToLibrary(game: Game, syncStatus: String): Flow<Resource<Game>> {
        return flow {
            emit(Resource.Loading(true))
            gamesDao.insertGame(game.toGameEntity(game.id, syncStatus = syncStatus))
            emit(Resource.Success(
                data = game
            ))
            emit(Resource.Loading(false))
            return@flow
        }
    }

    override suspend fun softDeleteGame(gameId: Int, sellPrice: Double): Flow<Resource<Game>> {
        return flow {
            emit(Resource.Loading(true))
            val gameToReturn: GameEntity = gamesDao.getGameById(gameId)
            gamesDao.updateGame(
                GameEntity(
                    id = gameToReturn.id,
                    name = gameToReturn.name,
                    producer = gameToReturn.producer,
                    purchaseDate = gameToReturn.purchaseDate,
                    lastTimePlayed = gameToReturn.lastTimePlayed,
                    isPlaying = gameToReturn.isPlaying,
                    price = gameToReturn.price,
                    currency = gameToReturn.currency,
                    platform = gameToReturn.platform,
                    formatOfGame = gameToReturn.formatOfGame,
                    picturePath = gameToReturn.picturePath,
                    syncFlag = "Delete"
                )
            )
            emit(Resource.Success(data = gameToReturn.toGame()))
            emit(Resource.Loading(false))
            return@flow
        }
    }

    override suspend fun hardDeleteGame(gameId: Int): Flow<Resource<Game>> {
        return flow {
            emit(Resource.Loading(true))
            val gameToDelete: GameEntity = gamesDao.getGameById(gameId)
            gamesDao.deleteGame(gameToDelete)
            emit(Resource.Success(data = gameToDelete.toGame()))
            emit(Resource.Loading(false))
            return@flow
        }
    }

    override suspend fun updateGame(game: Game, syncStatus: String): Flow<Resource<Game>> {
        return flow {
            emit(Resource.Loading(true))
            if (game.id == null) {
                emit(Resource.Failure(message = "Id can't be null"))
                return@flow
            }
            val gameToReturn: GameEntity = gamesDao.getGameById(game.id)
            gamesDao.updateGame(
                game.toGameEntity(game.id, syncStatus)
            )
            emit(Resource.Success(gameToReturn.toGame()))
            emit(Resource.Loading(false))
            return@flow
        }
    }
}