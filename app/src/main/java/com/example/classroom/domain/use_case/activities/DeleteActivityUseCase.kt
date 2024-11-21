package com.example.classroom.domain.use_case.activities

import com.example.classroom.data.repository.RepositoryBundle
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.Flow
import proyecto.person.appconsultapopular.common.Resource
import proyecto.person.appconsultapopular.common.apiUtils.catchError
import com.example.classroom.common.handlingError




class DeleteActivityUseCase(
    private val repositoryBundle: RepositoryBundle
) {
    suspend operator fun invoke(id:String) : Flow<Resource<Boolean>> {
        return handlingError<Boolean> {
            val data = repositoryBundle.activitiesRepository.deleteActivityRemote(id)
            if (data.statusCode.value == 200 || data.statusCode.value == 201) { // Allow both 200 and 201
                data.responseData!!
            }else{
                throw catchError(data.statusCode.value, null, message = data.messageError?.message)
            }
        }
    }
}