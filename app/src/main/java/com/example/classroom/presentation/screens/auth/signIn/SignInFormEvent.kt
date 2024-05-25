package com.example.classroom.presentation.screens.auth.signIn

sealed class SignInFormEvent {
    data class EmailChanged(val email: String) : SignInFormEvent()
    data class PasswordChanged(val password: String) : SignInFormEvent()
    object Submit: SignInFormEvent()
}