package com.example.gamecollection.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database (
    entities = [GameEntity::class],
    version = 4
        )
abstract class GamesDatabase: RoomDatabase() {
    abstract val gameDao: GameDao
}