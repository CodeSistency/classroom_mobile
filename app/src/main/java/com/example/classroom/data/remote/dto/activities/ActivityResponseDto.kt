package com.example.classroom.data.remote.dto.activities

import com.example.classroom.domain.model.entity.Status
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActivityResponseDto(
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
        @SerialName("id_course")
        val idCourse: String,
        @SerialName("owner")
        val owner: String,
        @SerialName("owner_name")
        val ownerName: String,
        @SerialName("title")
        val title: String,
        @SerialName("description")
        val description: String?,
        @SerialName("grade")
        val grade: Double? = null,
        @SerialName("start_date")
        val startDate: String,
        @SerialName("end_date")
        val endDate: String,
        @SerialName("status")
        val status: Status
    )
}
