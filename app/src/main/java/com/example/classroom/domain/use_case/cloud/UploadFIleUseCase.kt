package com.example.classroom.domain.use_case.cloud

import com.example.classroom.data.remote.dto.activities.ActivityRequestDto
import com.example.classroom.data.remote.dto.cloud.CloudResposeDto
import com.example.classroom.data.repository.RepositoryBundle
import com.example.classroom.domain.model.entity.LocalActivities
import com.example.classroom.domain.model.entity.toLocal
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.Flow
import proyecto.person.appconsultapopular.common.Resource
import proyecto.person.appconsultapopular.common.apiUtils.catchError
import proyecto.person.appconsultapopular.common.handlingError
import java.io.File



class UploadFileUseCase(
    private val repositoryBundle: RepositoryBundle
) {
    suspend operator fun invoke(file: File): Flow<Resource<CloudResposeDto>> {
        return handlingError<CloudResposeDto> {
            val data = repositoryBundle.cloudRepository.uploadFile(file)
            if (data.statusCode == HttpStatusCode.OK){
                data.responseData!!
            }else{
                throw catchError(data.statusCode.value, null, message = data.messageError?.message)
            }
        }
    }
}