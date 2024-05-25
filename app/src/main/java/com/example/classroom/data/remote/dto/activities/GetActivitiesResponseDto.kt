package com.example.classroom.data.remote.dto.activities

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.example.classroom.data.remote.dto.courses.CourseResponseDto
import com.example.classroom.domain.model.entity.Area
import com.example.classroom.domain.model.entity.Status
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetActivitiesResponseDto(
    @SerialName("status")
    val status: Boolean,
    @SerialName("message")
    val message: String,
    @SerialName("data")
    val data: Data
) {
    @Serializable
    data class Data(
        @SerialName("courses")
        val courses: List<Activties>
    ){
        @Serializable
        data class Activties(
            @SerialName("id") val id: String,
            @SerialName("idCourse") val idCourse: String,
            @SerialName("owner") val owner: String,
            @SerialName("owner_name") val ownerName: String,
            @SerialName("title") val title: String,
            @SerialName("description") val description: String?,
            @SerialName("grade") val grade: Double? = null,
            @SerialName("start_date") val startDate: String,
            @SerialName("end_date") val endDate: String,
            @SerialName("status") val status: Status,
        )
    }
}