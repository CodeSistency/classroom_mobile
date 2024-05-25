package com.example.classroom.common.validator

import com.example.classroom.domain.model.entity.Area

class CourseDataValidator {

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

    fun validateSection(input: String):  Result<Unit, InputError>{
        return validateString(input)
    }
    fun validateSubject(input: String):  Result<Unit, InputError>{
        return validateString(input)
    }

    fun validateArea(area: Area): Result<Unit, AreaError> {
        if(area == Area.NO_SELECTED) {
            return Result.Error(AreaError.NO_SELECTED)
        }
        return Result.Success(Unit)
    }
    enum class InputError: Error {
        TOO_SHORT,
    }
    enum class AreaError: Error {
        NO_SELECTED,
    }
}