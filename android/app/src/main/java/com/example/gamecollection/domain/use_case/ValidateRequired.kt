package com.example.gamecollection.domain.use_case

import javax.inject.Inject

class ValidateRequired @Inject constructor(){

    fun execute(fieldValue: String): ValidationResult {
        if (fieldValue.isEmpty()) {
            return ValidationResult(
                isSuccessful = false,
                errorMessage = "The field can't be empty"
            )
        }
        return ValidationResult(
            isSuccessful = true
        )
    }
}