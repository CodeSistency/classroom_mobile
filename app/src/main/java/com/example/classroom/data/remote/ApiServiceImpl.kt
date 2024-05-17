package com.example.classroom.data.remote

import com.example.classroom.data.remote.dto.login.signIn.SignInRequestDto
import com.example.classroom.data.remote.dto.login.signIn.SignInResponseDto
import io.ktor.client.HttpClient
import proyecto.person.appconsultapopular.common.ResponseGenericAPi

class ApiServiceImpl(private val client: HttpClient): ApiService {
    override suspend fun loginUser(loginRequestDto: SignInRequestDto): ResponseGenericAPi<SignInResponseDto> {
        TODO("Not yet implemented")
    }

}