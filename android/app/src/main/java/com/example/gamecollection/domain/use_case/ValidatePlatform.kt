package com.example.gamecollection.domain.use_case

import javax.inject.Inject

class ValidatePlatform @Inject constructor(){

    fun execute(fieldValue: String): ValidationResult {
        if (fieldValue.isEmpty()) {
            return ValidationResult(
                isSuccessful = false,
                errorMessage = "The field can't be empty"
            )
        }
        if (fieldValue != "PS" && fieldValue != "PC" && fieldValue != "XBOX") {
            return ValidationResult(
                isSuccessful = false,
                errorMessage = "The input is not a valid game platform"
            )
        }
        return ValidationResult(
            isSuccessful = true
        )
    }
}