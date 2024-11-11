package com.example.classroom.presentation.screens.submission.states

import com.example.classroom.domain.model.entity.LocalActivitySubmission
import proyecto.person.appconsultapopular.common.apiUtils.GenericCodeModel

data class SendActivityState(
    val isLoading: Boolean = false,
    val info: LocalActivitySubmission? = null,
    val error: GenericCodeModel? = null
)
