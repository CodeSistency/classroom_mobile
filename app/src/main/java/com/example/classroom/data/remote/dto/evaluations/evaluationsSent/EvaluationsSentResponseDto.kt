package com.example.classroom.data.remote.dto.evaluations.evaluationsSent

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class EvaluationsSentResponseDto(
    @SerialName("status")
    val status: Boolean,
    @SerialName("message")
    val message: String,
    @SerialName("data")
    val data: List<Data>
) {
    @Serializable
    data class Data(
        @SerialName("id")
        val idApi: Int,
        @SerialName("course_id")
        val idCourse: Int,
        @SerialName("course_name")
        val nameCourse: String,
        @SerialName("title")
        val title: String,
        @SerialName("description")
        val description: String?,
        @SerialName("grade")
        val grade: Int,
        @SerialName("start_date")
        val startDate: String,
        @SerialName("end_date")
        val endDate: String,
        @SerialName("status_id")
        val status: Int,
        @SerialName("status_name")
        val statusName: String,
        @SerialName("email")
        val email: String
    )
}