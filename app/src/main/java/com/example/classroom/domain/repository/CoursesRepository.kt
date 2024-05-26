package com.example.classroom.domain.repository

import com.example.classroom.data.remote.dto.courses.CourseRequestDto
import com.example.classroom.data.remote.dto.courses.CourseResponseDto
import com.example.classroom.data.remote.dto.courses.GetCoursesResponseDto
import com.example.classroom.domain.model.entity.LocalCourses
import kotlinx.coroutines.flow.Flow
import com.example.classroom.common.ResponseGenericAPi
import com.example.classroom.domain.model.entity.LocalUser

interface CoursesRepository {
    //Remote Methods
    suspend fun insertCourseRemote(course: CourseRequestDto) : ResponseGenericAPi<CourseResponseDto>
    suspend fun updateCourseRemote(course: CourseRequestDto, id: String) : ResponseGenericAPi<CourseResponseDto>
    suspend fun deleteCourseRemote(id: String) : ResponseGenericAPi<Boolean>
    suspend fun getCoursesRemote(id: String) : ResponseGenericAPi<GetCoursesResponseDto>
    suspend fun getCourseByIdRemote(id: String) : ResponseGenericAPi<CourseResponseDto>
    suspend fun getCoursesWithFlowRemote() : Flow<List<LocalCourses>>
    suspend fun joinCourseRemote(id: String, token: String) : ResponseGenericAPi<CourseResponseDto>
    suspend fun joinUserToCourseRemote(id: String, token: String) : ResponseGenericAPi<CourseResponseDto>


    //Local Methods
    suspend fun insertCourse(localCourses: LocalCourses)
    suspend fun insertAllCourses(localCourses: List<LocalCourses>)
    suspend fun updateCourse(localCourses: LocalCourses)
    suspend fun deleteCourse(id: String)
    suspend fun getCourses() : List<LocalCourses>
    suspend fun getCoursesWithFlow() : Flow<List<LocalCourses>>
    suspend fun getCoursesWithFlowById(id: String) : Flow<LocalCourses?>
    suspend fun getUsersByCourseWithFlow(id: String) : Flow<List<LocalUser>>

}