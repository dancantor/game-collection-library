package com.example.gamecollection.domain.use_case

import javax.inject.Inject

class ValidateFormat @Inject constructor(){

    fun execute(fieldValue: String): ValidationResult {
        if (fieldValue.isEmpty()) {
            return ValidationResult(
                isSuccessful = false,
                errorMessage = "The field can't be empty"
            )
        }

        if (fieldValue == "DISK" && fieldValue == "DIGITAL") {
            return ValidationResult(
                isSuccessful = false,
                errorMessage = "The input is not a valid format"
            )
        }
        return ValidationResult(
            isSuccessful = true
        )
    }
}