package com.example.classroom.presentation.screens.course.states

import com.example.classroom.domain.model.entity.LocalCourses
import com.example.classroom.domain.model.entity.LocalUser
import proyecto.person.appconsultapopular.common.apiUtils.GenericCodeModel

data class GetUsersState(
    val isLoading: Boolean = false,
    val info: List<LocalUser>? = null,
    val error: GenericCodeModel? = null
)
