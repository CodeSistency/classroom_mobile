package com.example.classroom.data.remote.dto.evaluations.reviewEvaluationDto

import com.example.classroom.domain.model.entity.Status
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ReviewEvaluationRequestDto(
    @SerialName("activity_id")
    val activityId: Int,
    @SerialName("grade")
    val grade: Int = 0,

)
