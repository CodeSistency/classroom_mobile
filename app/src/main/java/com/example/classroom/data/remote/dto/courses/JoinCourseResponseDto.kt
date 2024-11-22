package com.example.classroom.data.remote.dto.courses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class
JoinCourseResponseDto(
    @SerialName("code")
    val status: Int,
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
//        @SerialName("areaName")
//        val areaName: String,
        @SerialName("verified")
        val verified: Boolean
    )
}