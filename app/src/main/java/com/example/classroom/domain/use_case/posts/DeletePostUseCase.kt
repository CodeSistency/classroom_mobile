package com.example.classroom.domain.use_case.posts

import com.example.classroom.data.repository.RepositoryBundle
import com.example.classroom.domain.model.entity.LocalPost
import com.example.classroom.domain.model.entity.toLocal
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.Flow
import proyecto.person.appconsultapopular.common.Resource
import proyecto.person.appconsultapopular.common.apiUtils.catchError
import proyecto.person.appconsultapopular.common.handlingError



class DeletePostUseCase(
    private val repositoryBundle: RepositoryBundle
) {
    suspend operator fun invoke(courseId:String) : Flow<Resource<Boolean>> {
        return handlingError<Boolean> {
            val data = repositoryBundle.postsRepositoryImpl.deletePostRemote(courseId)
            if (data.statusCode == HttpStatusCode.OK){
                data.responseData!!
            }else{
                throw catchError(data.statusCode.value, null, message = data.messageError?.message)
            }
        }
    }
}