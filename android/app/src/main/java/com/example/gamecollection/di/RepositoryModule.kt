package com.example.gamecollection.di

import com.example.gamecollection.data.repository.GameRepositoryApiImpl
import com.example.gamecollection.data.repository.GameRepositoryImpl
import com.example.gamecollection.domain.repository.GameRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    @DaoRepository
    abstract fun bindGameRepository(
        gameRepositoryImpl: GameRepositoryImpl
    ): GameRepository

    @Binds
    @Singleton
    @ApiRepository
    abstract fun bindGameRepositoryApi(
        gameRepositoryApiImpl: GameRepositoryApiImpl
    ): GameRepository
}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class DaoRepository

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ApiRepository