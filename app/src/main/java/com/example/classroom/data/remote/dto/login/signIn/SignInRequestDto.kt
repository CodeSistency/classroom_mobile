package com.example.classroom.data.remote.dto.login.signIn

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class SignInRequestDto(
    @SerialName("email")
    val email: String,
    @SerialName("password")
    val password: String

)
