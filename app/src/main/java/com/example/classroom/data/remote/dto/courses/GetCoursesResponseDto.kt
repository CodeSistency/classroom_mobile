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
    val data: Data
) {
    @Serializable
    data class Data(
        @SerialName("courses")
        val courses: List<Courses>
    ){
        @Serializable
        data class Courses(
            @SerialName("id") val id: String,
            @SerialName("title") val title: String,
            @SerialName("description") val description: String?,
            @SerialName("owner") val owner: String,
            @SerialName("owner_name") val ownerName: String,
            @SerialName("section") val section: String,
            @SerialName("subject") val subject: String,
            @SerialName("area") val area: Area?,
            @SerialName("users") var users: List<CourseResponseDto.Data.Users>,
        )
    }
}

