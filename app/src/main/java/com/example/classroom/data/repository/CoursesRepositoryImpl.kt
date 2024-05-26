package com.example.classroom.data.repository

import com.example.classroom.data.local.db.AppDao
import com.example.classroom.data.remote.ApiService
import com.example.classroom.data.remote.dto.courses.CourseRequestDto
import com.example.classroom.data.remote.dto.courses.CourseResponseDto
import com.example.classroom.data.remote.dto.courses.GetCoursesResponseDto
import com.example.classroom.domain.model.entity.LocalCourses
import com.example.classroom.domain.repository.CoursesRepository
import kotlinx.coroutines.flow.Flow
import com.example.classroom.common.ResponseGenericAPi
import com.example.classroom.domain.model.entity.LocalUser

class CoursesRepositoryImpl(
    private val apiService: ApiService,
    private val dao: AppDao
): CoursesRepository {
    override suspend fun insertCourseRemote(course: CourseRequestDto): ResponseGenericAPi<CourseResponseDto> {
        return apiService.insertCourseRemote(course)
    }
    override suspend fun updateCourseRemote(course: CourseRequestDto, id: String): ResponseGenericAPi<CourseResponseDto> {
        return apiService.updateCourseRemote(course, id)
    }
    override suspend fun deleteCourseRemote(id: String): ResponseGenericAPi<Boolean> {
        return apiService.deleteCourseRemote(id)
    }
    override suspend fun getCoursesRemote(id: String): ResponseGenericAPi<GetCoursesResponseDto> {
        return apiService.getCoursesRemote(id)
    }

    override suspend fun getCourseByIdRemote(id: String): ResponseGenericAPi<CourseResponseDto> {
        return apiService.getCourseByIdRemote(id)
    }

    override suspend fun getCoursesWithFlowRemote(): Flow<List<LocalCourses>> {
        return apiService.getCoursesWithFlowRemote()
    }

    override suspend fun joinCourseRemote(
        id: String,
        token: String
    ): ResponseGenericAPi<CourseResponseDto> {
        return apiService.joinCourseRemote(id, token)
    }

    override suspend fun joinUserToCourseRemote(
        id: String,
        token: String
    ): ResponseGenericAPi<CourseResponseDto> {
        return apiService.joinUserToCourseRemote(id, token)
    }

    override suspend fun insertCourse(localCourses: LocalCourses) {
        return dao.insertLocalCourse(localCourses)
    }
    override suspend fun insertAllCourses(localCourses: List<LocalCourses>) {
        return dao.insertAllLocalCourse(localCourses)
    }
    override suspend fun updateCourse(localCourses: LocalCourses) {
        return dao.updateLocalCourse(localCourses)
    }
    override suspend fun deleteCourse(id: String) {
        return dao.deleteLocalCourseById(id)
    }
    override suspend fun getCourses(): List<LocalCourses> {
        return dao.getCoursesInfo()
    }
    override suspend fun getCoursesWithFlow(): Flow<List<LocalCourses>> {
        return dao.getCoursesInfoWithFlow()
    }
    override suspend fun getCoursesWithFlowById(id: String): Flow<LocalCourses?> {
        return dao.getCourseById(id)
    }

    override suspend fun getUsersByCourseWithFlow(id: String): Flow<List<LocalUser>> {
        return dao.getUsersByCourseIdWithFlow(id)
    }
}