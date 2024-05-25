package com.example.classroom.domain.use_case.activities

import com.example.classroom.data.remote.dto.activities.GetActivitiesResponseDto
import com.example.classroom.data.remote.dto.courses.GetCoursesResponseDto
import com.example.classroom.data.repository.RepositoryBundle
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.Flow
import proyecto.person.appconsultapopular.common.Resource
import proyecto.person.appconsultapopular.common.apiUtils.catchError
import proyecto.person.appconsultapopular.common.handlingError



class GetActivitiesUseCase(
    private val repositoryBundle: RepositoryBundle
) {
    suspend operator fun invoke(id: String) : Flow<Resource<GetActivitiesResponseDto>> {
        return handlingError<GetActivitiesResponseDto > {
            val data = repositoryBundle.activitiesRepository.getActivitiesRemote(id)
            if (data.statusCode == HttpStatusCode.OK){
                data.responseData!!
            }else{
                throw catchError(data.statusCode.value, null, message = data.messageError)
            }
        }
    }
}