package com.example.classroom.domain.use_case.activities

import com.example.classroom.data.remote.dto.activities.ActivityRequestDto
import com.example.classroom.data.remote.dto.courses.CourseRequestDto
import com.example.classroom.data.repository.RepositoryBundle
import com.example.classroom.domain.model.entity.LocalActivities
import com.example.classroom.domain.model.entity.LocalCourses
import com.example.classroom.domain.model.entity.toCourseLocal
import com.example.classroom.domain.model.entity.toLocal
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.Flow
import proyecto.person.appconsultapopular.common.Resource
import proyecto.person.appconsultapopular.common.apiUtils.catchError
import proyecto.person.appconsultapopular.common.handlingError

class InsertActivityUseCase(
    private val repositoryBundle: RepositoryBundle
) {
    suspend operator fun invoke(activity: ActivityRequestDto): Flow<Resource<LocalActivities>> {
        return handlingError<LocalActivities> {
            val data = repositoryBundle.activitiesRepository.insertActivityRemote(activity)
            if (data.statusCode == HttpStatusCode.OK){
                data.responseData?.toLocal()!!
            }else{
                throw catchError(data.statusCode.value, null, message = data.messageError)
            }
        }
    }
}