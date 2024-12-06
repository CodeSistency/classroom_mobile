package com.example.classroom.data.remote.dto.login.signUp

import com.example.classroom.domain.model.entity.Gender
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

//@Serializable
//data class SignUpRequestDto(
//    @SerialName("name")
//    val name: String,
//    @SerialName("lastname")
//    val lastname: String,
//    @SerialName("password")
//    val password: String,
//    @SerialName("email")
//    val email: String,
//    @SerialName("birthdate")
//    val birthdate: String,
//    @SerialName("phone")
//    val phone: String,
//    @SerialName("gender")
//    val gender: Gender,
//)

@Serializable
data class SignUpRequestDto(
    @SerialName("name")
    val name: String,
    @SerialName("last_name")
    val lastname: String,
    @SerialName("firebaseToken")
    val firebaseToken: String? = null,
    @SerialName("user_name")
    val username: String,
    @SerialName("password")
    val password: String,
    @SerialName("email")
    val email: String,
    @SerialName("birthdate")
    val birthdate: String,
    @SerialName("phone")
    val phone: String,
    @SerialName("genderId")
    val genderId: Int,
    @SerialName("roleId")
    val roleId: Int
)
