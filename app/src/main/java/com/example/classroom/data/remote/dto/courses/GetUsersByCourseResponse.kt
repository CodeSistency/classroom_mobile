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
        val users: List<User>
    ) {
        @Serializable
        data class User(
            @SerialName("id")
            val id: Int,
            @SerialName("userId")
            val userId: Int,
            @SerialName("courseId")
            val courseId: Int,
            @SerialName("user")
            val user: UserDetails
        ) {
            @Serializable
            data class UserDetails(
                @SerialName("id")
                val id: Int,
                @SerialName("email")
                val email: String,
                @SerialName("password")
                val password: String,
                @SerialName("create_date")
                val createDate: String,
                @SerialName("user_name")
                val userName: String,
                @SerialName("genderId")
                val genderId: Int,
                @SerialName("name")
                val name: String,
                @SerialName("last_name")
                val lastName: String,
                @SerialName("phone")
                val phone: String
            )
        }
    }
}