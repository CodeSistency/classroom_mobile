package com.example.classroom.domain.use_case.evaluations.getActivitiesSubmittedByStudent

import com.example.classroom.data.repository.RepositoryBundle
import com.example.classroom.domain.model.entity.LocalActivitySubmission
import com.example.classroom.domain.model.entity.toActivitiesSubmission
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.Flow
import proyecto.person.appconsultapopular.common.Resource
import proyecto.person.appconsultapopular.common.apiUtils.catchError
import com.example.classroom.common.handlingError



class GetActivitiesSubmitedByStudent(
    private val repositoryBundle: RepositoryBundle
) {
    suspend operator fun invoke(courseId:String, userId:String) : Flow<Resource<List<LocalActivitySubmission>>> {
        return handlingError<List<LocalActivitySubmission>> {
            val data = repositoryBundle.submissionsRepository.getActivitiesSubmited(courseId, userId)
            if (data.statusCode.value == 200 || data.statusCode.value == 201) { // Allow both 200 and 201
                data.responseData?.toActivitiesSubmission()!!
            }else{
                throw catchError(data.statusCode.value, null, message = data.messageError?.message)
            }
        }
    }
}