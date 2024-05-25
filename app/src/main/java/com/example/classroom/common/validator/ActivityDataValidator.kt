package com.example.classroom.common.validator

import com.example.classroom.domain.model.entity.Area

class ActivityDataValidator {
    fun validateString(input: String): Result<Unit, InputError> {
        if(input.length < 3) {
            return Result.Error(InputError.TOO_SHORT)
        }
        return Result.Success(Unit)
    }

    fun validateTitle(input: String):  Result<Unit, InputError>{
        return validateString(input)
    }

    fun validateDescription(input: String):  Result<Unit, InputError>{
        return validateString(input)
    }

    fun validateGrade(grade: Double): Result<Unit, GradeError> {
        if(grade <= 0) {
            return Result.Error(GradeError.TOO_LITTLE)
        }else if (grade > 100){
            return Result.Error(GradeError.TOO_MUCH)
        }
        return Result.Success(Unit)
    }

    fun validateDate(): Result<Unit, GradeError>{
        return Result.Success(Unit)
    }
    enum class InputError: Error {
        TOO_SHORT,
    }
    enum class DateError: Error {
        NO_SELECTED,
        TOO_OLD
    }

    enum class GradeError: Error {
        TOO_MUCH,
        TOO_LITTLE
    }
}