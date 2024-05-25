package com.example.classroom.domain.use_case.validators.cases

import android.util.Patterns
import com.example.classroom.domain.use_case.validators.ValidationResult
import java.text.SimpleDateFormat
import java.util.Locale

class ValidateDate {
    fun execute(date: String): ValidationResult {
        if (date.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "La fecha no puede estar en blanco"
            )
        }

        // Regex to match the "AA/MM/DD" format
        val datePattern = """\d{2}/\d{2}/\d{2}"""
        if (!date.matches(datePattern.toRegex())) {
            return ValidationResult(
                successful = false,
                errorMessage = "Formato de fecha no válido, debe ser AA/MM/DD"
            )
        }

        // Validate the date
        val dateFormat = SimpleDateFormat("aa/MM/dd", Locale.getDefault())
        dateFormat.isLenient = false
        return try {
            dateFormat.parse(date)
            ValidationResult(
                successful = true,
                errorMessage = null
            )
        } catch (e: Exception) {
            ValidationResult(
                successful = false,
                errorMessage = "Fecha no válida"
            )
        }
    }
}