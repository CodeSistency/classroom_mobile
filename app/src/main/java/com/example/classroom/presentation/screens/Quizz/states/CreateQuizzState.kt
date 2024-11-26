package com.example.classroom.presentation.screens.Quizz.states

import com.example.classroom.data.remote.dto.quizz.CreateQuizzResponseDto
import com.example.classroom.domain.model.entity.LocalCourses
import proyecto.person.appconsultapopular.common.apiUtils.GenericCodeModel

data class CreateQuizzState(
    val isLoading: Boolean = false,
    val info: CreateQuizzResponseDto? = null,
    val error: GenericCodeModel? = null
)