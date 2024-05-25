package com.example.classroom.domain.use_case.courses

import com.example.classroom.data.remote.dto.courses.GetCoursesResponseDto
import com.example.classroom.data.repository.RepositoryBundle
import com.example.classroom.domain.model.entity.LocalCourses
import com.example.classroom.domain.model.entity.toCourseLocal
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.Flow
import proyecto.person.appconsultapopular.common.Resource
import proyecto.person.appconsultapopular.common.apiUtils.catchError
import proyecto.person.appconsultapopular.common.handlingError

class GetCoursesUseCase(
    private val repositoryBundle: RepositoryBundle
) {
    suspend operator fun invoke(id: String) : Flow<Resource<GetCoursesResponseDto>>{
        return handlingError<GetCoursesResponseDto> {
            val data = repositoryBundle.coursesRepository.getCoursesRemote(id)
            if (data.statusCode == HttpStatusCode.OK){
                data.responseData!!
            }else{
                throw catchError(data.statusCode.value, null, message = data.messageError)
            }
        }
    }
}