package com.example.classroom.domain.use_case.courses

import com.example.classroom.data.repository.RepositoryBundle
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.Flow
import proyecto.person.appconsultapopular.common.Resource
import proyecto.person.appconsultapopular.common.apiUtils.catchError
import com.example.classroom.common.handlingError
import com.example.classroom.data.remote.dto.courses.DeleteCourseResponseDto

class DeleteCourseUseCase(
    private val repositoryBundle: RepositoryBundle
) {
    suspend operator fun invoke(id: String): Flow<Resource<DeleteCourseResponseDto>> {
        return handlingError<DeleteCourseResponseDto> {
            val data = repositoryBundle.coursesRepository.deleteCourseRemote(id)
            if (data.statusCode.value == 200 || data.statusCode.value == 201) { // Allow both 200 and 201
                data.responseData!!
            }else{
                throw catchError(data.statusCode.value, null, message = data.messageError?.message)
            }
        }
    }
}