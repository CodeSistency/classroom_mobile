package com.example.classroom.presentation.screens.activity.addActivity.states

import com.example.classroom.domain.model.entity.LocalActivities
import proyecto.person.appconsultapopular.common.apiUtils.GenericCodeModel

data class GetActivitiesState(
    val isLoading: Boolean = false,
    val info: List<LocalActivities>? = null,
    val error: GenericCodeModel? = null
)
