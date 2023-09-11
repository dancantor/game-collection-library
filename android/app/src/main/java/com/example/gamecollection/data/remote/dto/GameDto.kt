package com.example.gamecollection.data.remote.dto

import com.example.gamecollection.data.local.Currency
import com.example.gamecollection.data.local.Format
import com.example.gamecollection.data.local.Platform
import java.time.LocalDate
import java.time.LocalDateTime

data class GameDto (
    val id: Int?,
    val name: String,
    val producer: String,
    val purchaseDate: String,
    val lastTimePlayed: String,
    val price: Double,
    val currency: Currency,
    val platform: Platform,
    val formatOfGame: Format,
    val picturePath: String,
)