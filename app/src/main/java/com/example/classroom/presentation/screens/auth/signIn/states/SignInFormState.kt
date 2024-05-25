package com.example.classroom.presentation.screens.auth.signIn.states

import com.example.classroom.common.validator.UserDataValidator

data class SignInFormState(
    var email: String = "",
    var emailError: String? = null,
    var password: String = "",
    var passwordError: String? = null,
    )