package com.example.classroom.data.remote.dto.courses

import com.example.classroom.domain.model.entity.Area
import com.example.classroom.domain.model.entity.LocalUser
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CourseResponseDto(
    @SerialName("status")
    val status: Boolean,
    @SerialName("message")
    val message: String,
    @SerialName("data")
    val data: Data
) {
    @Serializable
    data class Data(
        @SerialName("id_api")
        val idApi: String,
        @SerialName("title")
        val title: String,
        @SerialName("description")
        val description: String?,
        @SerialName("owner")
        val owner: String,
        @SerialName("owner_name")
        val ownerName: String,
        @SerialName("section")
        val section: String,
        @SerialName("subject")
        val subject: String,
        @SerialName("area")
        val area: Area,
        @SerialName("users")
        val users: List<String>
    )
}