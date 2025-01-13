package com.example.classroom.data.remote.dto.quizz


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateQuizzResponseDto(
    val code: Int,
    val message: String,
    val data: CreatedQuizDataDto
)

@Serializable
data class CreatedQuizDataDto(
    val id: Int,
    @SerialName("activity_id")
    val activityId: Int,
    val activity: QuizActivityDto,
    val question: List<QuizQuestionDto>
)
