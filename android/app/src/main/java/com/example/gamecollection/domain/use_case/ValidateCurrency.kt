package com.example.gamecollection.domain.use_case

import javax.inject.Inject

class ValidateCurrency @Inject constructor(){

    fun execute(fieldValue: String): ValidationResult {
        if (fieldValue.isEmpty()) {
            return ValidationResult(
                isSuccessful = false,
                errorMessage = "The field can't be empty"
            )
        }
        if (fieldValue != "EUR" && fieldValue != "DOL" && fieldValue != "RON") {
            return ValidationResult(
                isSuccessful = false,
                errorMessage = "The input is not a valid currency"
            )
        }
        return ValidationResult(
            isSuccessful = true
        )
    }
}