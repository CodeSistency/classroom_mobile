package com.example.classroom.data.remote.dto.login

import com.example.classroom.domain.model.entity.Gender
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    @SerialName("id_api")
    val idApi: String,
    @SerialName("name")
    val name: String,
    @SerialName("lastname")
    val lastname: String,
    @SerialName("email")
    val email: String,
    @SerialName("gender")
    val gender: Gender,
    @SerialName("birthdate")
    val birthdate: String,
    @SerialName("phone")
    val phone: String
)
