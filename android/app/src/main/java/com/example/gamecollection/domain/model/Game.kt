package com.example.gamecollection.domain.model

import android.graphics.Bitmap
import com.example.gamecollection.data.local.Currency
import com.example.gamecollection.data.local.Format
import com.example.gamecollection.data.local.Platform
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

data class Game(
    val id: Int?,
    val name: String,
    val producer: String,
    val purchaseDate: LocalDate,
    val lastTimePlayed: LocalDateTime,
    val isPlaying: Boolean,
    val price: Double,
    val currency: Currency,
    val platform: Platform,
    val formatOfGame: Format,
    val picturePath: String,
    val syncFlag: String?
) {
    override fun equals(other: Any?): Boolean =
        other is Game &&
        name == other.name &&
        producer == other.producer &&
        purchaseDate == other.purchaseDate &&
        lastTimePlayed == other.lastTimePlayed &&
        price == other.price &&
        currency == other.currency &&
        platform == other.platform &&
        formatOfGame == other.formatOfGame &&
        picturePath == other.picturePath

}