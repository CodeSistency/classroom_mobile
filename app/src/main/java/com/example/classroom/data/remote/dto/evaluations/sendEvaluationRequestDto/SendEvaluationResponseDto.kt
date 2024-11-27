package com.example.classroom.data.remote.dto.evaluations.sendEvaluationRequestDto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class SendEvaluationResponseDto(
    @SerialName("code")
    val status: Int,
    @SerialName("message")
    val message: String,
    @SerialName("data")
    val data: Data
) {
    @Serializable
    data class Data(
        @SerialName("activityId")
        val activityId: Int,
        @SerialName("userId")
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


