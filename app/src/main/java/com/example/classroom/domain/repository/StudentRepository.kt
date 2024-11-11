package com.example.classroom.domain.repository

import com.example.classroom.common.ResponseGenericAPi
import com.example.classroom.data.remote.dto.courses.GetUsersByCourseResponse
import com.example.classroom.domain.model.entity.LocalStudents
import kotlinx.coroutines.flow.Flow

interface StudentRepository {

    suspend fun addStudentsToCourse(courseId: String, students: List<LocalStudents>)

    suspend fun getStudentsByCourseId(courseId: String): Flow<List<LocalStudents>> // Return as Flow

    suspend fun getStudentByApiIdAndCourse(idApi: String, courseId: String): LocalStudents?

    suspend fun deleteStudent(student: LocalStudents)

    suspend fun syncStudentsFromServer(courseId: String): ResponseGenericAPi<GetUsersByCourseResponse>
}