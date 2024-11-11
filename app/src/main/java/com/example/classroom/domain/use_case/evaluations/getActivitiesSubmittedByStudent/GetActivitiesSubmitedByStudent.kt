package com.example.classroom.domain.use_case.evaluations.getActivitiesSubmittedByStudent

import com.example.classroom.data.remote.dto.evaluations.evaluationsSent.EvaluationsSentResponseDto
import com.example.classroom.data.remote.dto.evaluations.reviewEvaluationDto.ReviewEvaluationRequestDto
import com.example.classroom.data.repository.RepositoryBundle
import com.example.classroom.domain.model.entity.LocalActivitySubmission
import com.example.classroom.domain.model.entity.toActivitiesSubmission
import com.example.classroom.domain.model.entity.toActivitySubmission
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.Flow
import proyecto.person.appconsultapopular.common.Resource
import proyecto.person.appconsultapopular.common.apiUtils.catchError
import proyecto.person.appconsultapopular.common.handlingError



class GetActivitiesSubmitedByStudent(
    private val repositoryBundle: RepositoryBundle
) {
    suspend operator fun invoke(courseId:String, userId:String) : Flow<Resource<List<LocalActivitySubmission>>> {
        return handlingError<List<LocalActivitySubmission>> {
            val data = repositoryBundle.submissionsRepository.getActivitiesSubmited(courseId, userId)
            if (data.statusCode == HttpStatusCode.OK){
                data.responseData?.toActivitiesSubmission()!!
            }else{
                throw catchError(data.statusCode.value, null, message = data.messageError?.message)
            }
        }
    }
}