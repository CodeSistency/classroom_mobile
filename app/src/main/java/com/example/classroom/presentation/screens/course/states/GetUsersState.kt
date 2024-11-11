package com.example.classroom.presentation.screens.course.states

import com.example.classroom.domain.model.entity.LocalCourses
import com.example.classroom.domain.model.entity.LocalStudents
import com.example.classroom.domain.model.entity.LocalUser
import proyecto.person.appconsultapopular.common.apiUtils.GenericCodeModel

data class GetUsersState(
    val isLoading: Boolean = false,
    val info: List<LocalStudents>? = null,
    val error: GenericCodeModel? = null
)
