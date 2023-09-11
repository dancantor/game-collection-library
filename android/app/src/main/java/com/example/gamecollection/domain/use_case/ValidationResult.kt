package com.example.gamecollection.domain.use_case

data class ValidationResult (
    val isSuccessful: Boolean,
    val errorMessage: String? = null
) {

}