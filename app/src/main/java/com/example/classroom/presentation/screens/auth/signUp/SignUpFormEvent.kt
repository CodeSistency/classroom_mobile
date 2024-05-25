package com.example.classroom.presentation.screens.auth.signUp

import com.example.classroom.domain.model.entity.Gender


sealed class SignUpFormEvent {
    data class NameChanged(val name : String)  : SignUpFormEvent()
    data class LastnameChanged(val lastname : String)  : SignUpFormEvent()
    data class EmailChanged(val email: String) : SignUpFormEvent()
    data class PhoneChanged(val phone : String)  : SignUpFormEvent()
//    data class AgeChanged(val phone : String)  : SignUpFormEvent()
    data class GenderChanged(val gender : Gender)  : SignUpFormEvent()
    data class PasswordChanged(val password: String) : SignUpFormEvent()
    object Submit: SignUpFormEvent()
}