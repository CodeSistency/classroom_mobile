package com.example.classroom.presentation.screens.home.states

import proyecto.person.appconsultapopular.common.apiUtils.GenericCodeModel

data class DeleteCourseState(

    val isLoading: Boolean = false,
    val info: String? = null,
    val error: GenericCodeModel? = null
)

