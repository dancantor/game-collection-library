package com.example.gamecollection.di

import android.app.Application
import androidx.room.Room
import com.example.gamecollection.data.local.GamesDatabase
import com.example.gamecollection.data.local.MIGRATION_1_2
import com.example.gamecollection.data.local.MIGRATION_2_3
import com.example.gamecollection.data.local.MIGRATION_3_4
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGameDatabase(app: Application): GamesDatabase {
        return Room.databaseBuilder(
            app,
            GamesDatabase::class.java,
            "gamedb.db"
        )
            .build()
    }

}