package com.example.classroom.domain.repository

import com.example.classroom.data.remote.dto.login.signIn.SignInRequestDto
import com.example.classroom.data.remote.dto.login.signIn.SignInResponseDto
import com.example.classroom.data.remote.dto.login.signUp.SignUpRequestDto
import com.example.classroom.data.remote.dto.login.signUp.SignUpResponseDto
import proyecto.person.appconsultapopular.common.ResponseGenericAPi

interface LoginRepository{
    suspend fun authUser(user: SignInRequestDto) : ResponseGenericAPi<SignInResponseDto>
    suspend fun registerUser(user: SignUpRequestDto) : ResponseGenericAPi<SignUpResponseDto>
    suspend fun logout()
}
