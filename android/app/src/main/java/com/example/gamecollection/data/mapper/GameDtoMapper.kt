package com.example.gamecollection.data.mapper

import com.example.gamecollection.data.local.GameEntity
import com.example.gamecollection.data.remote.dto.GameDto
import com.example.gamecollection.domain.model.Game
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun GameDto.toGame(): Game {
    return Game(
        id = this.id,
        name = this.name,
        producer = this.producer,
        purchaseDate = LocalDate.parse(this.purchaseDate, DateTimeFormatter.ofPattern("dd-MM-yyyy")),
        lastTimePlayed = LocalDateTime.parse(this.lastTimePlayed, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")),
        isPlaying = false,
        price = this.price,
        currency = this.currency,
        platform = this.platform,
        formatOfGame = this.formatOfGame,
        picturePath = this.picturePath,
        syncFlag = "UpToDate"
    )
}

fun Game.toGameDto(id: Int? = null): GameDto {
    return GameDto(
        id = id,
        name = this.name,
        producer = this.producer,
        purchaseDate = this.purchaseDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
        lastTimePlayed = this.lastTimePlayed.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")),
        price = this.price,
        currency = this.currency,
        platform = this.platform,
        formatOfGame = this.formatOfGame,
        picturePath = this.picturePath,
    )
}