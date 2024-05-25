package com.example.classroom.presentation.screens.home.states

import com.example.classroom.domain.model.entity.LocalCourses
import proyecto.person.appconsultapopular.common.apiUtils.GenericCodeModel

data class CourseState(
    val isLoading: Boolean = false,
    val info: List<LocalCourses>? = null,
    val error: GenericCodeModel? = null
)
