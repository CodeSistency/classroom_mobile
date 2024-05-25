package com.example.classroom.domain.repository

import com.example.classroom.data.remote.dto.login.signIn.SignInRequestDto
import com.example.classroom.data.remote.dto.login.signIn.SignInResponseDto
import com.example.classroom.data.remote.dto.login.signUp.SignUpRequestDto
import com.example.classroom.data.remote.dto.login.signUp.SignUpResponseDto
import com.example.classroom.domain.model.entity.LocalUser
import kotlinx.coroutines.flow.Flow
import com.example.classroom.common.ResponseGenericAPi

interface LoginRepository{
    suspend fun authUser(user: SignInRequestDto) : ResponseGenericAPi<SignInResponseDto>
    suspend fun registerUser(user: SignUpRequestDto) : ResponseGenericAPi<SignUpResponseDto>

    /** insert info user*/
    suspend fun insertLocalUser(localUser: LocalUser)

    /** update info user*/
    suspend fun updateLocalUser(localUser: LocalUser)

    /** delete info user*/
    suspend fun deleteLocalUser()

    /** get info user*/
    suspend fun getUserInfo(): List<LocalUser>

    suspend fun getUserLogged(): Flow<LocalUser?>

    fun getUserInfoWithFlow(): Flow<List<LocalUser>>

    fun getUserInfoByIdWithFlow(userId: Int): Flow<LocalUser?>

    suspend fun logout()
}
