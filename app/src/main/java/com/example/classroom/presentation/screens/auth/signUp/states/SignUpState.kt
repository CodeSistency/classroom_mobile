package com.example.classroom.presentation.screens.auth.signUp.states

import com.example.classroom.domain.model.entity.LocalUser
import proyecto.person.appconsultapopular.common.apiUtils.GenericCodeModel

data class SignUpState (
    val isLoading: Boolean = false,
    val info: LocalUser? = null,
    val error: GenericCodeModel? = null
)