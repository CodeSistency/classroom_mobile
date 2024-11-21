package com.example.classroom.domain.use_case.courses

import android.util.Log
import com.example.classroom.data.remote.dto.courses.CourseRequestDto
import com.example.classroom.data.repository.RepositoryBundle
import com.example.classroom.domain.model.entity.LocalCourses
import com.example.classroom.domain.model.entity.toCourseLocal
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.Flow
import proyecto.person.appconsultapopular.common.Resource
import proyecto.person.appconsultapopular.common.apiUtils.catchError
import com.example.classroom.common.handlingError

class InsertCourseUseCase(
    private val repositoryBundle: RepositoryBundle
) {
    suspend operator fun invoke(course: CourseRequestDto): Flow<Resource<LocalCourses>> {
        return handlingError<LocalCourses> {
            val data = repositoryBundle.coursesRepository.insertCourseRemote(course)

            Log.e("response", data.toString())
            if (data.statusCode.value == 200 || data.statusCode.value == 201) { // Allow both 200 and 201
                data.responseData?.toCourseLocal()!!
            }else{
                throw catchError(data.statusCode.value, null, message = data.messageError?.message)
            }
        }
    }

}