package com.example.classroom.domain.use_case.validators.cases

import android.util.Patterns
import com.example.classroom.common.validator.Result
import com.example.classroom.common.validator.UserDataValidator
import com.example.classroom.domain.use_case.validators.ValidationResult

class ValidatePassword {
    fun execute(password: String): ValidationResult {
//        if(password.isBlank()) {
//            return ValidationResult(
//                successful = false,
//                errorMessage = "El contenido no puede estar en blanco"
//            )
//        }
        if(password.length < 9) {
            return ValidationResult(
                successful = false,
                errorMessage = "La contraseña debe ser mayor a 9 caracteres"
            )
        }

//        val hasUppercaseChar = password.any { it.isUpperCase() }
//        if(!hasUppercaseChar) {
//            return ValidationResult(
//                successful = false,
//                errorMessage = "La contraseña debe tener una mayuscula"
//            )
//        }
//
//        val hasDigit = password.any { it.isDigit() }
//        if(!hasDigit) {
//            return ValidationResult(
//                successful = false,
//                errorMessage = "La contraseña debe tener un digito"
//            )
//        }

        return ValidationResult(
            successful = true,
            errorMessage = null
        )
    }
}