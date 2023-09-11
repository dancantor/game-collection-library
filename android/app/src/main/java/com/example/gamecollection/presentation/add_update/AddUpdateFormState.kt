package com.example.gamecollection.presentation.add_update

import com.example.gamecollection.data.local.Currency
import com.example.gamecollection.data.local.Format
import com.example.gamecollection.data.local.Platform
import java.time.LocalDateTime

data class AddUpdateFormState (
    val name: String = "",
    val nameError: String? = null,
    val producer: String = "",
    val producerError: String? = null,
    val purchaseDate: String = "",
    val purchaseDateError: String? = null,
    val lastTimePlayed: String = "",
    val lastTimePlayedError: String? = null,
    val isPlaying: Boolean = false,
    val isPlayingError: String? = null,
    val price: String = "",
    val priceError: String? = null,
    val currency: String = "",
    val currencyError: String? = null,
    val platform: String = "",
    val platformError: String? = null,
    val formatOfGame: String = "",
    val formatOfGameError: String? = null,
    val picturePath: String = "",
    val picturePathError: String? = null,
) {
}