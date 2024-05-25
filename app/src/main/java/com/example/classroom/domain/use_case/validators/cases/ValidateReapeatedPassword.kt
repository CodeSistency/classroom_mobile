package com.example.classroom.domain.use_case.validators.cases

import com.example.classroom.domain.use_case.validators.ValidationResult

class ValidateReapeatedPassword {
    fun execute(password: String, repeatedPassword: String): ValidationResult {
        if(password.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "El contenido no puede estar en blanco"
            )
        }
        if(password != repeatedPassword) {
            return ValidationResult(
                successful = false,
                errorMessage = "The passwords don't match"
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}