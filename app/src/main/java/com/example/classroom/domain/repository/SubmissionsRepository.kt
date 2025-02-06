package com.example.classroom.domain.repository

import com.example.classroom.common.ResponseGenericAPi
import com.example.classroom.data.remote.dto.evaluations.evaluationsSent.EvaluationsSentResponseDto
import com.example.classroom.data.remote.dto.evaluations.reviewEvaluationDto.ReviewEvaluationRequestDto
import com.example.classroom.data.remote.dto.evaluations.reviewEvaluationDto.ReviewEvaluationsResponseDto
import com.example.classroom.data.remote.dto.evaluations.sendEvaluationRequestDto.SendEvaluationRequestDto
import com.example.classroom.data.remote.dto.evaluations.sendEvaluationRequestDto.SendEvaluationResponseDto
import com.example.classroom.domain.model.entity.LocalActivitySubmission
import com.example.classroom.domain.model.entity.StudentProgress

import kotlinx.coroutines.flow.Flow
import okhttp3.Response
import java.io.File

interface SubmissionsRepository {

    suspend fun addOrUpdateSubmission(submission: LocalActivitySubmission)

    suspend fun getSubmissionsForStudent(activityId: String, studentId: String): Flow<List<LocalActivitySubmission>> // Return as Flow

    suspend fun getSubmissionsForStudentByCourse(studentId: String, courseId: String): Flow<List<LocalActivitySubmission>> // Return as Flow


    suspend fun getAllSubmissions(): Flow<List<LocalActivitySubmission>> // Return as Flow


    suspend fun addSubmissionsWithoutDuplicates(submissions: List<LocalActivitySubmission>)

    suspend fun getAllSubmissionsForActivity(activityId: String): Flow<List<LocalActivitySubmission>> // Return as Flow

    fun getTotalPonderation(studentId: String, courseId: String): Flow<Int>

    fun getStudentProgress(studentId: String, courseId: String): Flow<StudentProgress>



    //REMOTE

    suspend fun getActivitiesSubmited(courseId: String, userId: String): ResponseGenericAPi<EvaluationsSentResponseDto>

    suspend fun submitActivityToServerAndSync(submission: SendEvaluationRequestDto): ResponseGenericAPi<SendEvaluationResponseDto>


    suspend fun reviewActivityToServerAndSync(review: ReviewEvaluationRequestDto): ResponseGenericAPi<ReviewEvaluationsResponseDto>





}