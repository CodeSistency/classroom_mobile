package com.example.classroom.data.remote.dto.login.signIn

import com.example.classroom.domain.model.entity.Gender
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignInResponseDto(
    @SerialName("status")
    val status: Boolean,
    @SerialName("message")
    val message: String,
    @SerialName("data")
    val data: Data
) {
    @Serializable
    data class Data(
        @SerialName("user_id")
        val userId: String,
        @SerialName("name")
        val name: String,
        @SerialName("lastname")
        val lastame: String,
        @SerialName("email")
        val email: String,
        @SerialName("phone")
        val phone: String,
        @SerialName("birthdate")
        val birthdate: String,
        @SerialName("gender")
        val gender: Gender,
    )
}
