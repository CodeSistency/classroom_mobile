package com.example.classroom.domain.use_case.evaluations.professorReviewsActivityUseCase

import com.example.classroom.data.remote.dto.evaluations.reviewEvaluationDto.ReviewEvaluationRequestDto
import com.example.classroom.data.remote.dto.evaluations.reviewEvaluationDto.ReviewEvaluationsResponseDto
import com.example.classroom.data.repository.RepositoryBundle
import com.example.classroom.domain.model.entity.LocalActivitySubmission
import com.example.classroom.domain.model.entity.LocalCourses
import com.example.classroom.domain.model.entity.toActivitySubmission
import com.example.classroom.domain.model.entity.toCourseLocal
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.Flow
import proyecto.person.appconsultapopular.common.Resource
import proyecto.person.appconsultapopular.common.apiUtils.catchError
import proyecto.person.appconsultapopular.common.handlingError



class ProfessorReviewsActivityUseCase(
    private val repositoryBundle: RepositoryBundle
) {
    suspend operator fun invoke(body: ReviewEvaluationRequestDto) : Flow<Resource<LocalActivitySubmission>> {
        return handlingError<LocalActivitySubmission> {
            val data = repositoryBundle.submissionsRepository.reviewActivityToServerAndSync(body)
            if (data.statusCode == HttpStatusCode.OK){
                data.responseData?.toActivitySubmission()!!
            }else{
                throw catchError(data.statusCode.value, null, message = data.messageError?.message)
            }
        }
    }
}