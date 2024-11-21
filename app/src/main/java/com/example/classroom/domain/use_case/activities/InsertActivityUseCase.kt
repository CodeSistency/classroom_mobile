package com.example.classroom.domain.use_case.activities

import com.example.classroom.data.remote.dto.activities.ActivityRequestDto
import com.example.classroom.data.repository.RepositoryBundle
import com.example.classroom.domain.model.entity.LocalActivities
import com.example.classroom.domain.model.entity.toLocal
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.Flow
import proyecto.person.appconsultapopular.common.Resource
import proyecto.person.appconsultapopular.common.apiUtils.catchError
import com.example.classroom.common.handlingError

class InsertActivityUseCase(
    private val repositoryBundle: RepositoryBundle
) {
    suspend operator fun invoke(activity: ActivityRequestDto): Flow<Resource<LocalActivities>> {
        return handlingError<LocalActivities> {
            val data = repositoryBundle.activitiesRepository.insertActivityRemote(activity)
            if (data.statusCode.value == 200 || data.statusCode.value == 201) { // Allow both 200 and 201
                data.responseData?.toLocal()!!
            }else{
                throw catchError(data.statusCode.value, null, message = data.messageError?.message)
            }
        }
    }
}