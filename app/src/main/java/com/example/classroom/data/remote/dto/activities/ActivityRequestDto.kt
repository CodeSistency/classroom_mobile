package com.example.classroom.data.remote.dto.activities

import com.example.classroom.domain.model.entity.Status
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActivityRequestDto(

    @SerialName("id_course")
    val idCourse: String,
//    @SerialName("owner")
//    val owner: String,
//    @SerialName("owner_name")
//    val ownerName: String,
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String?,
    @SerialName("grade")
    val grade: Int = 0,
    @SerialName("email")
    val email: String,
    @SerialName("start_date")
    val startDate: String,
    @SerialName("end_date")
    val endDate: String,
    @SerialName("status")
    val status: Status
)