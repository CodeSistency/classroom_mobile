package com.example.classroom.domain.use_case.signUp

import android.util.Log
import com.example.classroom.data.remote.dto.login.signIn.SignInRequestDto
import com.example.classroom.data.remote.dto.login.signUp.SignUpRequestDto
import com.example.classroom.data.remote.dto.login.signUp.SignUpResponseDto
import com.example.classroom.data.repository.RepositoryBundle
import com.example.classroom.domain.model.entity.LocalUser
import com.example.classroom.domain.model.entity.toLoginLocal
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.Flow
import proyecto.person.appconsultapopular.common.Resource
import proyecto.person.appconsultapopular.common.apiUtils.catchError
import proyecto.person.appconsultapopular.common.handlingError
import timber.log.Timber

class SignUpUseCase(
    private val repositoryBundle: RepositoryBundle
) {
    suspend operator fun invoke(user: SignUpRequestDto): Flow<Resource<LocalUser>> {
        return handlingError<LocalUser> {
            val data = repositoryBundle.loginRepository.registerUser(
                user
            )


            if (data.statusCode == HttpStatusCode.OK){
                repositoryBundle.loginRepository.logout()
                data.responseData!!.toLoginLocal()
            }else{
                throw catchError(data.statusCode.value, null, message = data.messageError)
            }
        }
    }
}