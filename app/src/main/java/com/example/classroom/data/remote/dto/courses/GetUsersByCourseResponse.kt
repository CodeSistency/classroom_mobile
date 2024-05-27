package com.example.classroom.data.remote.dto.courses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetUsersByCourseResponse(
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
        val idApi: Int,
        @SerialName("title")
        val title: String,
        @SerialName("description")
        val description: String?,
        @SerialName("ownerId")
        val owner: Int,
        @SerialName("owner_name")
        val ownerName: String,
        @SerialName("section")
        val section: String,
        @SerialName("subject")
        val subject: String,
        @SerialName("areaId")
        val areaId: Int,
        @SerialName("token")
        val token: String,
        @SerialName("areaName")
        val areaName: String,
        @SerialName("users")
        val users: List<Users>
    ){
        @Serializable
        data class Users(
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
}
