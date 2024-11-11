package com.example.classroom.presentation.screens.activity.studentEvaluations.states

import com.example.classroom.domain.model.entity.LocalActivitySubmission
import proyecto.person.appconsultapopular.common.apiUtils.GenericCodeModel

data class StudentEvaluationsState(
    val isLoading: Boolean = false,
    val info: List<LocalActivitySubmission>? = null,
    val error: GenericCodeModel? = null
)
