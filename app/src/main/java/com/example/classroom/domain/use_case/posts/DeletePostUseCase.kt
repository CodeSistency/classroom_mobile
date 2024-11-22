package com.example.classroom.domain.use_case.posts

import com.example.classroom.data.repository.RepositoryBundle
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.Flow
import proyecto.person.appconsultapopular.common.Resource
import proyecto.person.appconsultapopular.common.apiUtils.catchError
import com.example.classroom.common.handlingError
import com.example.classroom.data.remote.dto.posts.PostResponseDto


class DeletePostUseCase(
    private val repositoryBundle: RepositoryBundle
) {
    suspend operator fun invoke(courseId:String) : Flow<Resource<PostResponseDto>> {
        return handlingError<PostResponseDto> {
            val data = repositoryBundle.postsRepositoryImpl.deletePostRemote(courseId)
            if (data.statusCode.value == 200 || data.statusCode.value == 201) { // Allow both 200 and 201
                data.responseData!!
            }else{
                throw catchError(data.statusCode.value, null, message = data.messageError?.message)
            }
        }
    }
}