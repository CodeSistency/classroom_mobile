package com.example.classroom.domain.use_case.signIn

import com.example.classroom.data.remote.dto.login.signIn.SignInRequestDto
import com.example.classroom.data.repository.RepositoryBundle
import com.example.classroom.domain.model.entity.LocalUser
import com.example.classroom.domain.model.entity.toLoginLocal
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.Flow
import proyecto.person.appconsultapopular.common.Resource
import proyecto.person.appconsultapopular.common.apiUtils.catchError
import proyecto.person.appconsultapopular.common.handlingError


class SignInUseCase (
    private val repositoryBundle: RepositoryBundle
) {
        suspend operator fun invoke(user: String, password: String): Flow<Resource<LocalUser>> {
            return handlingError<LocalUser> {
                val data = repositoryBundle.loginRepository.authUser(
                    SignInRequestDto(
                        user,
                        password
                    )
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