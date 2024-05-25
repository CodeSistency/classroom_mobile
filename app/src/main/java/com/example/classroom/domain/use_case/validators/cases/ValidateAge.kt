package com.example.classroom.domain.use_case.validators.cases

import com.example.classroom.domain.use_case.validators.ValidationResult
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class ValidateAge {
    fun execute(birthdate: String): ValidationResult {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        dateFormat.isLenient = false

        return try {
            val date = dateFormat.parse(birthdate)
            val age = calculateAge(date)
            when {
                age < 16 ->  return ValidationResult(
                    successful = false,
                    errorMessage = "Edad invalida, muy joven"
                )
                age > 40 ->  return ValidationResult(
                    successful = false,
                    errorMessage = "Excede la edad"
                )
                else ->  return ValidationResult(
                    successful = true,
                    errorMessage = null
                )
            }
        } catch (e: Exception) {
            return ValidationResult(
                successful = false,
                errorMessage = "Edad invalida"
            )
        }
    }
}

fun calculateAge(birthdate: Date): Int {
    val now = Calendar.getInstance()
    val birth = Calendar.getInstance()
    birth.time = birthdate

    var age = now.get(Calendar.YEAR) - birth.get(Calendar.YEAR)
    if (now.get(Calendar.DAY_OF_YEAR) < birth.get(Calendar.DAY_OF_YEAR)) {
        age--
    }
    return age
}