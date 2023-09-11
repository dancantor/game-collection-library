package com.example.gamecollection.domain.repository

import com.example.gamecollection.domain.model.Game
import com.example.gamecollection.util.Resource
import kotlinx.coroutines.flow.Flow

interface GameRepository {
    fun getGamesListings(): Flow<Resource<List<Game>>>

    suspend fun getGameById(
        gameId: Int
    ): Flow<Resource<Game>>

    suspend fun addGameToLibrary(
        game: Game,
        syncStatus: String = "UpToDate"
    ): Flow<Resource<Game>>

    suspend fun softDeleteGame (
        gameId: Int,
        sellPrice: Double
    ): Flow<Resource<Game>>

    suspend fun hardDeleteGame(
        gameId: Int
    ): Flow<Resource<Game>>

    suspend fun updateGame (
        game: Game,
        syncStatus: String = "UpToDate"
    ): Flow<Resource<Game>>
}