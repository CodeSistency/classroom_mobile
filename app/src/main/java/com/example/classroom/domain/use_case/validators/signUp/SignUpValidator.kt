package com.example.classroom.domain.use_case.validators.signUp

import com.example.classroom.domain.use_case.validators.cases.ValidateAge
import com.example.classroom.domain.use_case.validators.cases.ValidateEmail
import com.example.classroom.domain.use_case.validators.cases.ValidateNames
import com.example.classroom.domain.use_case.validators.cases.ValidatePassword
import com.example.classroom.domain.use_case.validators.cases.ValidatePhone
import com.example.classroom.domain.use_case.validators.cases.ValidateReapeatedPassword

data class SignUpValidator(
    val validateNames: ValidateNames,
    val validatePassword: ValidatePassword,
    val validateEmail: ValidateEmail,
    val validateReapeatedPassword: ValidateReapeatedPassword,
    val validateAge: ValidateAge,
    val validatePhone: ValidatePhone,
)
