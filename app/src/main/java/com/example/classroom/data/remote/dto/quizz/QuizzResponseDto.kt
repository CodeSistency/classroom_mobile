package com.example.classroom.data.remote.dto.quizz

import kotlinx.serialization.Serializable

@Serializable
data class QuizzResponseDto(
    val text: String,               // The question text
                 // The index of the correct answer (0-based)
)