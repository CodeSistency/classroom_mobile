package com.example.classroom.data.remote

import com.example.classroom.data.remote.dto.login.signIn.SignInRequestDto
import com.example.classroom.data.remote.dto.login.signIn.SignInResponseDto
import proyecto.person.appconsultapopular.common.ResponseGenericAPi

interface ApiService {

    suspend fun loginUser(loginRequestDto: SignInRequestDto): ResponseGenericAPi<SignInResponseDto>

}