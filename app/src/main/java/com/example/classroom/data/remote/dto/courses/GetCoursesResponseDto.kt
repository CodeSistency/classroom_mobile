package com.example.classroom.data.remote.dto.courses

import androidx.room.ColumnInfo
import com.example.classroom.domain.model.entity.Area
import com.example.classroom.domain.model.entity.Gender
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetCoursesResponseDto(
    @SerialName("status")
    val status: Boolean,
    @SerialName("message")
    val message: String,
    @SerialName("data")
    val data: List<Data>
) {
    @Serializable
    data class Data(
        @SerialName("id") val id: Int,
        @SerialName("title") val title: String,
        @SerialName("description") val description: String,
        @SerialName("ownerId") val owner: Int,
        @SerialName("owner_name") val ownerName: String,
        @SerialName("section") val section: String,
        @SerialName("subject") val subject: String,
        @SerialName("areaId") val areaId: Int,
        @SerialName("token") val token: String,
        @SerialName("areaName") val areaName: String
    )
}

