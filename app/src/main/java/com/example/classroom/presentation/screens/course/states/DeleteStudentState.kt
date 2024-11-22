package com.example.classroom.presentation.screens.course.states

import com.example.classroom.domain.model.entity.LocalCourses
import proyecto.person.appconsultapopular.common.apiUtils.GenericCodeModel

data class DeleteStudentState(
    val isLoading: Boolean = false,
    val info: Boolean? = null,
    val error: GenericCodeModel? = null
)
