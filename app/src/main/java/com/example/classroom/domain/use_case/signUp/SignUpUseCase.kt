package com.example.classroom.domain.use_case.signUp

import com.example.classroom.data.remote.dto.login.signIn.SignInRequestDto
import com.example.classroom.data.remote.dto.login.signUp.SignUpRequestDto
import com.example.classroom.data.repository.RepositoryBundle
import com.example.classroom.domain.model.entity.LocalUser
import com.example.classroom.domain.model.entity.toLoginLocal
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.Flow
import proyecto.person.appconsultapopular.common.Resource
import proyecto.person.appconsultapopular.common.apiUtils.catchError
import proyecto.person.appconsultapopular.common.handlingError

class SignUpUseCase(
    private val repositoryBundle: RepositoryBundle
) {
    suspend operator fun invoke(user: SignUpRequestDto): Flow<Resource<Boolean>> {
        return handlingError<Boolean> {
            val data = repositoryBundle.loginRepository.registerUser(
                user = user
            )
            if (data.statusCode == HttpStatusCode.OK){
                repositoryBundle.loginRepository.logout()
                true
            }else{
                throw catchError(data.statusCode.value, null, message = data.messageError)
            }
        }
    }
}