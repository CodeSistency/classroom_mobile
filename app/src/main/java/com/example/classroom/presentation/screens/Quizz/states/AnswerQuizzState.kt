package com.example.classroom.presentation.screens.Quizz.states

import com.example.classroom.data.remote.dto.quizz.AnswerQuizzResponseDto
import proyecto.person.appconsultapopular.common.apiUtils.GenericCodeModel


data class AnswerQuizzState(
    val isLoading: Boolean = false,
    val info: AnswerQuizzResponseDto? = null,
    val error: GenericCodeModel? = null
)
