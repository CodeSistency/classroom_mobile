package com.example.classroom.domain.use_case.posts

import com.example.classroom.data.repository.RepositoryBundle
import com.example.classroom.domain.model.entity.LocalActivitySubmission
import com.example.classroom.domain.model.entity.LocalPost
import com.example.classroom.domain.model.entity.toActivitiesSubmission
import com.example.classroom.domain.model.entity.toLocal
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.Flow
import proyecto.person.appconsultapopular.common.Resource
import proyecto.person.appconsultapopular.common.apiUtils.catchError
import proyecto.person.appconsultapopular.common.handlingError



class GetPostsUseCase(
    private val repositoryBundle: RepositoryBundle
) {
    suspend operator fun invoke(courseId:String) : Flow<Resource<List<LocalPost>>> {
        return handlingError<List<LocalPost>> {
            val data = repositoryBundle.postsRepositoryImpl.getPostByCourseRemote(courseId)
            if (data.statusCode == HttpStatusCode.OK){
                data.responseData?.toLocal()!!
            }else{
                throw catchError(data.statusCode.value, null, message = data.messageError?.message)
            }
        }
    }
}