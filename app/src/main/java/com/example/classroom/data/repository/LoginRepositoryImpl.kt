package com.example.classroom.data.repository

import com.example.classroom.data.remote.ApiService
import com.example.classroom.data.remote.dto.login.signIn.SignInRequestDto
import com.example.classroom.data.remote.dto.login.signIn.SignInResponseDto
import com.example.classroom.data.remote.dto.login.signUp.SignUpRequestDto
import com.example.classroom.data.remote.dto.login.signUp.SignUpResponseDto
import com.example.classroom.domain.repository.LoginRepository
import proyecto.person.appconsultapopular.common.ResponseGenericAPi
import com.example.classroom.data.local.db.AppDao

class LoginRepositoryImpl(
    private val apiService: ApiService,
    private val dao: AppDao
): LoginRepository {
    override suspend fun authUser(user: SignInRequestDto): ResponseGenericAPi<SignInResponseDto> {
        TODO("Not yet implemented")
    }

    override suspend fun registerUser(user: SignUpRequestDto): ResponseGenericAPi<SignUpResponseDto> {
        TODO("Not yet implemented")
    }

    override suspend fun logout() {
        TODO("Not yet implemented")
    }

}