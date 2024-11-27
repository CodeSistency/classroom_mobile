package com.example.classroom.data.remote.dto.evaluations.evaluationsSent

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class EvaluationsSentResponseDto(
    @SerialName("code")
    val status: Int,
    @SerialName("message")
    val message: String,
    @SerialName("data")
    val data: List<Data>
) {
    @Serializable
    data class Data(
        @SerialName("activity_id")
        val activityId: Int,
        @SerialName("user_id")
        val userId: Int,
        @SerialName("document")
        val document: String,
        @SerialName("message")
        val message: String,
        @SerialName("grade")
        val grade: Double,
        @SerialName("create_date")
        val createDate: String,
    )
}