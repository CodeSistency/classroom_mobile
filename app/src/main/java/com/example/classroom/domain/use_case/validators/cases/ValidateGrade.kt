package com.example.classroom.domain.use_case.validators.cases

import android.util.Patterns
import com.example.classroom.domain.use_case.validators.ValidationResult

class ValidateGrade {
    fun execute(grade: Double?): ValidationResult {
        if(grade == null) {
            return ValidationResult(
                successful = false,
                errorMessage = "La nota no puede estar vacia"
            )
        }
        if(grade > 100) {
            return ValidationResult(
                successful = false,
                errorMessage = "La nota no puede ser mayor a 100"
            )
        }
        if(grade < 0) {
            return ValidationResult(
                successful = false,
                errorMessage = "La nota no puede ser menor 0"
            )
        }
        return ValidationResult(
            successful = true,
            errorMessage = null
        )
    }
}