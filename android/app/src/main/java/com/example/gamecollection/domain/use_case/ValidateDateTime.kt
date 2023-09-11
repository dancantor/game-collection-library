package com.example.gamecollection.domain.use_case

import javax.inject.Inject

class ValidateDateTime @Inject constructor() {

    fun execute(fieldValue: String): ValidationResult {
        if (fieldValue.isEmpty()) {
            return ValidationResult(
                isSuccessful = false,
                errorMessage = "The field can't be empty"
            )
        }
        //"mm/dd/yyyy hh:MM"
        val dateTimeRegex = "^(3[01]|[12][0-9]|0[1-9]|[1-9])/(1[0-2]|0[1-9]|[1-9])/[0-9]{4} [0-9][0-9]:([0]?[0-5][0-9]|[0-9])$".toRegex()
        if (!dateTimeRegex.matches(fieldValue)) {
            return ValidationResult(
                isSuccessful = false,
                errorMessage = "The date time is not valid"
            )
        }
        return ValidationResult(
            isSuccessful = true
        )
    }
}