package com.example.classroom.data.repository

import com.example.classroom.data.remote.ApiService
import com.example.classroom.data.remote.dto.login.signIn.SignInRequestDto
import com.example.classroom.data.remote.dto.login.signIn.SignInResponseDto
import com.example.classroom.data.remote.dto.login.signUp.SignUpRequestDto
import com.example.classroom.data.remote.dto.login.signUp.SignUpResponseDto
import com.example.classroom.domain.repository.LoginRepository
import com.example.classroom.common.ResponseGenericAPi
import com.example.classroom.data.local.db.AppDao
import com.example.classroom.domain.model.entity.LocalUser
import kotlinx.coroutines.flow.Flow

class LoginRepositoryImpl(
    private val apiService: ApiService,
    private val dao: AppDao
): LoginRepository {
    override suspend fun authUser(user: SignInRequestDto): ResponseGenericAPi<SignInResponseDto> {
        return apiService.signInUser(user)
    }

    override suspend fun registerUser(user: SignUpRequestDto): ResponseGenericAPi<SignUpResponseDto> {
        return apiService.signUpUser(user)
    }

    override suspend fun insertLocalUser(localUser: LocalUser) {
        dao.insertLocalUser(localUser)
    }

    override suspend fun updateLocalUser(localUser: LocalUser) {
        dao.updateLocalUser(localUser)
    }

    override suspend fun deleteLocalUser() {
        dao.deleteLocalUser()
    }

    override suspend fun getUserInfo(): List<LocalUser> {
        return dao.getUserInfo()
    }

    override suspend fun getUserLogged(): Flow<LocalUser?> {
        return dao.getLoggedInUser()
    }

    override fun getUserInfoWithFlow(): Flow<List<LocalUser>> {
        return dao.getUserInfoWithFlow()
    }

    override fun getUserInfoByIdWithFlow(userId: Int): Flow<LocalUser?> {
        return dao.getUserById(userId)
    }

    override suspend fun logout() {
        dao.logout()
    }

}