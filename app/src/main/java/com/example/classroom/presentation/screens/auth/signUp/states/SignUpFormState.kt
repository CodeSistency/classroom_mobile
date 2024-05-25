package com.example.classroom.presentation.screens.auth.signUp.states

import com.example.classroom.domain.model.entity.Gender

data class SignUpFormState(
    var name: String = "",
    var nameError: String? = null,
    var lastname: String = "",
    var lastnameError: String? = null,
    var email: String = "",
    var emailError: String? = null,
    var birthdate: String = "",
    var birthdateError: String? = null,
    var gender: Gender = Gender.Man,
    val genderError: String? = null,
    var phone: String = "",
    var phoneError: String? = null,
    var password: String = "",
    var passwordError: String? = null

    )
