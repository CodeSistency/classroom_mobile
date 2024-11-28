package com.example.classroom.data.remote.dto.quizz

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@kotlinx.serialization.Serializable
data class QuizOptionDto(
    val id: Int,
    val text: String
)

@kotlinx.serialization.Serializable
data class QuizQuestionDto(
    val id: Int,
    val text: String,
    val answer: Int,
    val options: List<QuizOptionDto>
)

@kotlinx.serialization.Serializable
data class QuizActivityDto(
    val id: Int,
    val title: String,
    val description: String,
    val grade: Double,
    @SerialName("start_date")
    val startDate: String,
    @SerialName("end_date")
    val endDate: String,
    val email: String,
    val digital: Boolean,
    val isQuizz: Boolean
)

@kotlinx.serialization.Serializable
data class QuizSubmissionDto(
    val id: Int,
    val quizzId: Int,
    @SerialName("user_id")
    val userId: Int,
    val grade: Double,
    val answers: List<SubmittedAnswerDto>
)

@kotlinx.serialization.Serializable
data class SubmittedAnswerDto(
    val id: Int,
    val optionId: Int
)

@kotlinx.serialization.Serializable
data class AnswerQuizzResponseDto(
    val code: Int,
    val message: String,
    val data: AnswerQuizzDataDto
)

@kotlinx.serialization.Serializable
data class AnswerQuizzDataDto(
    val grade: Double,
    val submission: QuizSubmissionDto,
    val quizz: QuizDto,
    val quizzId: Int, // Add the quiz ID explicitly
)

@Serializable
data class QuizDto(
    val id: Int,
    val activityId: Int,
    val activity: QuizActivityDto,
    val question: List<QuizQuestionDto>
)
