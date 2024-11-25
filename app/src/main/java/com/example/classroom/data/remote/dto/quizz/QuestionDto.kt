package com.example.classroom.data.remote.dto.quizz

import kotlinx.serialization.Serializable

@Serializable
data class QuestionDto(
    val text: String,               // The question text
    val options: MutableList<String>, // Use MutableList instead of List
    val answer: Int                 // The index of the correct answer (0-based)
)