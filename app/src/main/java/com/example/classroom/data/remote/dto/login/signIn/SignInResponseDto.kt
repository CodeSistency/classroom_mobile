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
        @SerialName("id")
        val userId: Int,
        @SerialName("email")
        val email: String,
        @SerialName("password")
        val password: String,
        @SerialName("user_name")
        val username: String,
        @SerialName("create_date")
        val creation: String,
        @SerialName("genderId")
        val gender: Int,
        @SerialName("phone")
        val phone: String,
        @SerialName("name")
        val name: String,
        @SerialName("last_name")
        val lastName: String
    )
}
