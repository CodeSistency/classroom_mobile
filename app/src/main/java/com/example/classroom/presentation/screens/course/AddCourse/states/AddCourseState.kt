package com.example.classroom.presentation.screens.course.AddCourse.states

import com.example.classroom.domain.model.entity.LocalCourses
import proyecto.person.appconsultapopular.common.apiUtils.GenericCodeModel

data class AddCourseState(
    val isLoading: Boolean = false,
    val info: LocalCourses? = null,
    val error: GenericCodeModel? = null
)
