package com.example.classroom.data.remote.dto.quizz

import kotlinx.serialization.Serializable

@Serializable
data class AnswerQuizzDto(
    val userId: Int,                // The ID of the user submitting the answers
    val answers: List<AnswerDto>    // A list of answers provided by the user
)

@Serializable
data class AnswerDto(
    val questionId: Int,            // The ID of the question
    val optionId: Int               // The ID of the selected option
)