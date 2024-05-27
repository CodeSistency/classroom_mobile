package com.example.classroom.domain.use_case.courses

import com.example.classroom.data.repository.RepositoryBundle
import com.example.classroom.domain.model.entity.LocalUser
import com.example.classroom.domain.model.entity.toLocal
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.Flow
import proyecto.person.appconsultapopular.common.Resource
import proyecto.person.appconsultapopular.common.apiUtils.catchError
import proyecto.person.appconsultapopular.common.handlingError

class GetUsersByCourseUseCase (
    private val repositoryBundle: RepositoryBundle
) {
    suspend operator fun invoke(id: String): Flow<Resource<List<LocalUser>>> {
        return handlingError<List<LocalUser>> {
            val data = repositoryBundle.coursesRepository.getUsersByCourseRemote(id)
            if (data.statusCode == HttpStatusCode.OK){
                data.responseData?.toLocal()!!
            }else{
                throw catchError(data.statusCode.value, null, message = data.messageError)
            }
        }
    }

}