package com.example.gamecollection.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class Currency(val sign: String) {
    EUR("€"), DOL("$"), RON("RON")
}

enum class Platform {
    PC, XBOX, PS
}

enum class Format {
    DIGITAL, DISK
}

@Entity
data class GameEntity(
    @PrimaryKey(autoGenerate = false) val id: Int? = null,
    val name: String,
    val producer: String,
    val purchaseDate: String,
    val lastTimePlayed: String,
    val isPlaying: Boolean,
    val price: Double,
    val currency: Currency,
    val platform: Platform,
    val formatOfGame: Format,
    val picturePath: String,
    val syncFlag: String?
) {
}