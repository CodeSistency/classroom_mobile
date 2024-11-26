package com.example.classroom.data.remote.dto.quizz

import kotlinx.serialization.Serializable

@Serializable
data class CreateQuizzDto(
    val title: String,                // The title of the quiz
    val description: String,          // The description of the quiz
    val grade: Int,                   // The grade associated with the quiz
    val startDate: String,            // The start date of the quiz
    val endDate: String,              // The end date of the quiz
    val email: String,                // The email of the creator
    val digital: Boolean,             // Whether the quiz is digital
    val statusId: Int,                // The status ID of the quiz
    val courseId: Int,                // The course ID associated with the quiz
    val questions: List<Question>  // The list of questions
)

@Serializable
data class Question(
    val text: String,                // The question text
    val options: List<String>,
    val answer: Int                  // The index of the correct answer (0-based)
)