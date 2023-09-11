package com.example.gamecollection.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {
    @Insert(onConflict =  OnConflictStrategy.REPLACE)
    suspend fun insertGame (
        gameEntity: GameEntity
    )

    @Query(
        """
            SELECT * 
            FROM gameentity
        """
    )
    fun getAllGames(): Flow<List<GameEntity>>

    @Query(
        """
            SELECT *
            FROM gameentity
            WHERE id = :id
        """
    )
    suspend fun getGameById(id: Int): GameEntity

    @Delete
    suspend fun deleteGame(gameEntity: GameEntity)

    @Update
    suspend fun updateGame(gameEntity: GameEntity)

}