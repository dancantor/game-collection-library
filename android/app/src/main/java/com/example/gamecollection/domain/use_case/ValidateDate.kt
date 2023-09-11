package com.example.gamecollection.domain.use_case

import javax.inject.Inject

class ValidateDate @Inject constructor(){

    fun execute(fieldValue: String): ValidationResult {
        if (fieldValue.isEmpty()) {
            return ValidationResult(
                isSuccessful = false,
                errorMessage = "The field can't be empty"
            )
        }
        //"mm/dd/yyyy"
        val dateRegex = "^(3[01]|[12][0-9]|0[1-9]|[1-9])/(1[0-2]|0[1-9]|[1-9])/[0-9]{4}$".toRegex()
        if (!dateRegex.matches(fieldValue)) {
            return ValidationResult(
                isSuccessful = false,
                errorMessage = "The date is not valid"
            )
        }
        return ValidationResult(
            isSuccessful = true
        )
    }
}