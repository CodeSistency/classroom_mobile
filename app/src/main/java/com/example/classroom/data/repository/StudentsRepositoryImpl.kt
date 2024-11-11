package com.example.classroom.data.repository

import com.example.classroom.common.ResponseGenericAPi
import com.example.classroom.data.local.db.AppDao
import com.example.classroom.data.remote.ApiService
import com.example.classroom.data.remote.dto.courses.GetUsersByCourseResponse
import com.example.classroom.domain.model.entity.LocalStudents
import com.example.classroom.domain.repository.StudentRepository
import kotlinx.coroutines.flow.Flow

class StudentsRepositoryImpl(
    private val apiService: ApiService,
    private val dao: AppDao
): StudentRepository {

    override suspend fun addStudentsToCourse(courseId: String, students: List<LocalStudents>) {
        dao.addStudentsToCourse(courseId, students)
    }

    // Get all students for a specific course
    override suspend fun getStudentsByCourseId(courseId: String): Flow<List<LocalStudents>> {
        return dao.getStudentsByCourseId(courseId)
    }

    // Get a student by ID for a specific course
    override suspend fun getStudentByApiIdAndCourse(idApi: String, courseId: String): LocalStudents? {
        return dao.getStudentByApiIdAndCourse(idApi, courseId)
    }

    // Delete a student from a course
    override suspend fun deleteStudent(student: LocalStudents) {
        dao.deleteStudent(student)
    }

    override suspend fun syncStudentsFromServer(courseId: String): ResponseGenericAPi<GetUsersByCourseResponse> {
        return apiService.getUsersByCourseRemote(courseId)
    }

    // Fetch students from the server and add them to the local database
//    suspend fun syncStudentsFromServer(courseId: Int) {
//        val response = apiService.getStudents(courseId) // Assuming this API call exists
//        if (response.isSuccessful) {
//            val students = response.body()?.map { it.toLocalStudent(courseId) } ?: emptyList()
//            addStudentsToCourse(courseId, students)
//        }
//    }
}