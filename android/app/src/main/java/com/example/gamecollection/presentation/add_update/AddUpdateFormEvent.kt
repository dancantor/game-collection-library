package com.example.gamecollection.presentation.add_update

sealed class AddUpdateFormEvent {
    data class NameChanged(val name: String): AddUpdateFormEvent()
    data class ProducerChanged(val producer: String): AddUpdateFormEvent()
    data class PurchaseDateChanged(val purchaseDate: String): AddUpdateFormEvent()
    data class LastTimePlayedChanged(val lastTimePlayed: String): AddUpdateFormEvent()
    data class PriceChanged(val price: String): AddUpdateFormEvent()
    data class CurrencyChanged(val currency: String): AddUpdateFormEvent()
    data class PlatformChanged(val platform: String): AddUpdateFormEvent()
    data class FormatChanged(val format: String): AddUpdateFormEvent()
    data class PictureChanged(val pictureURL: String): AddUpdateFormEvent()

    object Submit: AddUpdateFormEvent()
}