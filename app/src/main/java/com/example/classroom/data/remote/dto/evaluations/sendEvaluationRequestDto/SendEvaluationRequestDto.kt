package com.example.classroom.data.remote.dto.evaluations.sendEvaluationRequestDto

import com.example.classroom.domain.model.entity.Status
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class SendEvaluationRequestDto(
    @SerialName("user_id")
    val userId: Int,
    @SerialName("activity_id")
    val activityId: Int,
    @SerialName("message")
    val message: String,
    @SerialName("document")
    val document: String
)