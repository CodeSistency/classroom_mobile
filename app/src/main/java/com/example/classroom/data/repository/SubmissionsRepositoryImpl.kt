package com.example.classroom.data.repository

import com.example.classroom.common.ResponseGenericAPi
import com.example.classroom.data.local.db.daos.AppDao
import com.example.classroom.data.remote.ApiService
import com.example.classroom.data.remote.dto.evaluations.evaluationsSent.EvaluationsSentResponseDto
import com.example.classroom.data.remote.dto.evaluations.reviewEvaluationDto.ReviewEvaluationRequestDto
import com.example.classroom.data.remote.dto.evaluations.reviewEvaluationDto.ReviewEvaluationsResponseDto
import com.example.classroom.data.remote.dto.evaluations.sendEvaluationRequestDto.SendEvaluationRequestDto
import com.example.classroom.data.remote.dto.evaluations.sendEvaluationRequestDto.SendEvaluationResponseDto
import com.example.classroom.domain.model.entity.LocalActivitySubmission
import com.example.classroom.domain.model.entity.StudentProgress
import com.example.classroom.domain.repository.SubmissionsRepository
import kotlinx.coroutines.flow.Flow

class SubmissionsRepositoryImpl(
    private val apiService: ApiService,
    private val dao: AppDao
): SubmissionsRepository {

    override suspend fun addOrUpdateSubmission(submission: LocalActivitySubmission) {
        dao.insertOrUpdateSubmission(submission)
    }

    override suspend fun getSubmissionsForStudent(activityId: String, studentId: String): Flow<List<LocalActivitySubmission>> {
        return dao.getSubmissionsForStudent(activityId, studentId)
    }

    override suspend fun getSubmissionsForStudentByCourse(studentId: String, courseId: String): Flow<List<LocalActivitySubmission>> {
        return dao.getSubmissionsForStudentByCourse(studentId, courseId)
    }

    override suspend fun addSubmissionsWithoutDuplicates(submissions: List<LocalActivitySubmission>) {
        return dao.addSubmissionsWithoutDuplicates(submissions)
    }


    override suspend fun getAllSubmissionsForActivity(activityId: String): Flow<List<LocalActivitySubmission>> {
        return dao.getAllSubmissionsForActivity(activityId)
    }

    override fun getTotalPonderation(studentId: String, courseId: String): Flow<Int> {
        return dao.getTotalPonderation(studentId, courseId)
    }

    override fun getStudentProgress(
        studentId: String,
        courseId: String
    ): Flow<StudentProgress> {
        return dao.getStudentProgress(studentId, courseId)
    }


    override suspend fun getActivitiesSubmited(
        courseId: String,
        userId: String
    ): ResponseGenericAPi<EvaluationsSentResponseDto> {
        return apiService.getActivitiesSentByStudent(courseId, userId)
    }

    override suspend fun submitActivityToServerAndSync(submission: SendEvaluationRequestDto): ResponseGenericAPi<SendEvaluationResponseDto> {
        return apiService.studentSendsEvaluation(submission)
    }

    override suspend fun reviewActivityToServerAndSync(review: ReviewEvaluationRequestDto): ResponseGenericAPi<ReviewEvaluationsResponseDto> {
        return apiService.professorReviewsEvaluation(review)
    }


}