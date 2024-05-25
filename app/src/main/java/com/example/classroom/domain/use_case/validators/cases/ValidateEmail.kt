package com.example.classroom.domain.use_case.validators.cases

import android.util.Patterns
import com.example.classroom.domain.use_case.validators.ValidationResult

class ValidateEmail {
    fun execute(email: String): ValidationResult {
        if(email.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "El email no puede estar en blanco"
            )
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Este no es un email valido"
            )
        }
        return ValidationResult(
            successful = true,
            errorMessage = null
        )
    }
}