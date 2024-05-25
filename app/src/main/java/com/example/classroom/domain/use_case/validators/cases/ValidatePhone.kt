package com.example.classroom.domain.use_case.validators.cases

import com.example.classroom.common.validator.Result
import com.example.classroom.common.validator.UserDataValidator
import com.example.classroom.domain.use_case.validators.ValidationResult

class ValidatePhone {
    fun execute(phone: String): ValidationResult {
        val phoneRegex = "^\\+?[1-9]\\d{1,14}$".toRegex()

        if(phone.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "El contenido no puede estar en blanco"
            )
        }// E.164 international phone number format
        return if (phone.matches(phoneRegex)) {
            ValidationResult(
                successful = true,
                errorMessage = null
            )
        } else {
            ValidationResult(
                successful = false,
                errorMessage = "Numero de telefono invalido"
            )
        }
    }
}