package com.example.classroom.domain.use_case.validators.cases

import com.example.classroom.common.validator.Result
import com.example.classroom.common.validator.UserDataValidator
import com.example.classroom.domain.use_case.validators.ValidationResult

class ValidateNames {

    fun execute(name: String): ValidationResult {
        if(name.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "El contenido no puede estar en blanco"
            )
        }
        if(name.length < 2) {
            return ValidationResult(
                successful = false,
                errorMessage = "El contenido es muy corto"
            )
        }

        val hasDigit = name.any { it.isDigit() }
        if (hasDigit) { // Corrected this condition
            return ValidationResult(
                successful = false,
                errorMessage = "El contenido es invalido. Contiene numeros"
            )
        }

        return ValidationResult(
            successful = true,
            errorMessage = null
        )
    }
}