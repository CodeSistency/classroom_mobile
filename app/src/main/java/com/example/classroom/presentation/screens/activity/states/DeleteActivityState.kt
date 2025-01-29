package com.example.classroom.presentation.screens.activity.states

import com.example.classroom.domain.model.entity.LocalActivities
import proyecto.person.appconsultapopular.common.apiUtils.GenericCodeModel

data class DeleteActivityState(
    val isLoading: Boolean = false,
    val info: Boolean? = null,
    val error: GenericCodeModel? = null
)