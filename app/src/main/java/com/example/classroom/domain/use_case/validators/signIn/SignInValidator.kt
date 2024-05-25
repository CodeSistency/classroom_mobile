package com.example.classroom.domain.use_case.validators.signIn

import com.example.classroom.domain.use_case.validators.cases.ValidateEmail
import com.example.classroom.domain.use_case.validators.cases.ValidatePassword

data class SignInValidator(
    val validateEmail: ValidateEmail,
    val validatePassword: ValidatePassword
)
