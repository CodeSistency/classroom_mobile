package com.example.classroom.common.validator

import com.example.classroom.data.remote.dto.login.signUp.SignUpRequestDto
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class UserDataValidator {

    fun validateName(name: String): Result<Unit, NameError> {
        if(name.length < 2) {
            return Result.Error(NameError.TOO_SHORT)
        }

        val hasDigit = name.any { it.isDigit() }
        if (hasDigit) { // Corrected this condition
            return Result.Error(NameError.HAS_DIGIT)
        }

        return Result.Success(Unit)
    }
    fun validatePassword(password: String): Result<Unit, PasswordError> {
        if(password.length < 9) {
            return Result.Error(PasswordError.TOO_SHORT)
        }

        val hasUppercaseChar = password.any { it.isUpperCase() }
        if(!hasUppercaseChar) {
            return Result.Error(PasswordError.NO_UPPERCASE)
        }

        val hasDigit = password.any { it.isDigit() }
        if(!hasDigit) {
            return Result.Error(PasswordError.NO_DIGIT)
        }

        return Result.Success(Unit)
    }


    fun validateEmail(email: String): Result<Unit, EmailError> {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
        return if (email.matches(emailRegex)) {
            Result.Success(Unit)
        } else {
            Result.Error(EmailError.INVALIDATE)
        }
    }

    fun validatePhone(phone: String): Result<Unit, PhoneError> {
        val phoneRegex = "^\\+?[1-9]\\d{1,14}$".toRegex() // E.164 international phone number format
        return if (phone.matches(phoneRegex)) {
            Result.Success(Unit)
        } else {
            Result.Error(PhoneError.INVALIDATE)
        }
    }

    fun validateBirthdate(birthdate: String): Result<Unit, BirthdateError> {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        dateFormat.isLenient = false

        return try {
            val date = dateFormat.parse(birthdate)
            val age = calculateAge(date)
            when {
                age < 16 -> Result.Error(BirthdateError.TOO_YOUNG)
                age > 40 -> Result.Error(BirthdateError.TOO_OLD)
                else -> Result.Success(Unit)
            }
        } catch (e: Exception) {
            Result.Error(BirthdateError.INVALIDATE)
        }
    }

    private fun calculateAge(birthdate: Date): Int {
        val now = Calendar.getInstance()
        val birth = Calendar.getInstance()
        birth.time = birthdate

        var age = now.get(Calendar.YEAR) - birth.get(Calendar.YEAR)
        if (now.get(Calendar.DAY_OF_YEAR) < birth.get(Calendar.DAY_OF_YEAR)) {
            age--
        }
        return age
    }


    enum class EmailError: Error {
        REPEATED,
        INVALIDATE,
    }

    enum class NameError: Error {
        TOO_SHORT,
        HAS_DIGIT,
    }

    enum class BirthdateError: Error {
        TOO_OLD,
        TOO_YOUNG,
        INVALIDATE,
    }

    enum class PhoneError: Error {
        REPEATED,
        INVALIDATE,
    }

    enum class PasswordError: Error {
        TOO_SHORT,
        NO_UPPERCASE,
        NO_DIGIT
    }
}