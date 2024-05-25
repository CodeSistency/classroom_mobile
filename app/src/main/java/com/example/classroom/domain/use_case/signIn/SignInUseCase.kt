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
import timber.log.Timber


class SignInUseCase  (
    private val repositoryBundle: RepositoryBundle
) {
        suspend operator fun invoke(user: SignInRequestDto): Flow<Resource<LocalUser>> {
            return handlingError<LocalUser> {
                val data = repositoryBundle.loginRepository.authUser(
                    user
                )
                Timber.tag("responseData").e(data.responseData.toString())

                if (data.statusCode == HttpStatusCode.OK){
                    repositoryBundle.loginRepository.logout()
                    Timber.tag("dataaaLogin").e(data.responseData.toString())

                    data.responseData!!.toLoginLocal()
                }else{
                    Timber.tag("dataaaLoginError").e(data.responseData.toString())

                    throw catchError(data.statusCode.value, null, message = data.messageError)
                }
            }
        }

}