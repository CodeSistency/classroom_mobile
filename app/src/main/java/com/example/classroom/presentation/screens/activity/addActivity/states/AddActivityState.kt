package com.example.classroom.presentation.screens.activity.addActivity.states

import com.example.classroom.domain.model.entity.LocalActivities
import com.example.classroom.domain.model.entity.LocalCourses
import proyecto.person.appconsultapopular.common.apiUtils.GenericCodeModel

data class AddActivityState(
    val isLoading: Boolean = false,
    val info: LocalActivities? = null,
    val error: GenericCodeModel? = null
)
