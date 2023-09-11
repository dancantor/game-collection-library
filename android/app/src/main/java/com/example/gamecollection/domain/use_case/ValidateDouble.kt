package com.example.gamecollection.domain.use_case

import javax.inject.Inject

class ValidateDouble @Inject constructor() {
    fun execute(fieldValue: String): ValidationResult {
        if (fieldValue.isEmpty()) {
            return ValidationResult(
                isSuccessful = false,
                errorMessage = "The field can't be empty"
            )
        }
        if (fieldValue.toDoubleOrNull() == null) {
            return ValidationResult(
                isSuccessful = false,
                errorMessage = "The field must be in valid double format"
            )
        }
        return ValidationResult(
            isSuccessful = true
        )
    }
}