package com.example.gamecollection.data.remote.api

import com.example.gamecollection.data.remote.dto.GameDto
import retrofit2.Response
import retrofit2.http.*

interface GameLibraryApi {
    @GET("game")
    suspend fun getGames(): Response<List<GameDto>>

    @GET("game/{id}")
    suspend fun getGameById(@Path("id") id: Int): Response<GameDto>

    @POST("game")
    suspend fun addNewGame(@Body game: GameDto): Response<GameDto>

    @DELETE("game/{id}")
    suspend fun deleteGame(@Path("id") id: Int): Response<GameDto>

    @PUT("game/{id}")
    suspend fun updateGame(@Body game: GameDto, @Path("id") id: Int): Response<GameDto>
}