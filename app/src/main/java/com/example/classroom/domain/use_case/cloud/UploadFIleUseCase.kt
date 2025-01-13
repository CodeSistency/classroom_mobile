package com.example.classroom.domain.use_case.cloud

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.classroom.data.remote.dto.cloud.CloudResposeDto
import com.example.classroom.data.repository.RepositoryBundle
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.Flow
import proyecto.person.appconsultapopular.common.Resource
import proyecto.person.appconsultapopular.common.apiUtils.catchError
import com.example.classroom.common.handlingError
import java.io.File



class UploadFileUseCase(
    private val repositoryBundle: RepositoryBundle
) {
    suspend operator fun invoke(fileUri: Uri, context: Context, useSupase: Boolean): Flow<Resource<CloudResposeDto>> {
        return handlingError<CloudResposeDto> {
            val data = repositoryBundle.cloudRepository.uploadFile(fileUri, context, useSupase)
            Log.e("response cloud", data.toString())
            if (data.statusCode.value == 200 || data.statusCode.value == 201) { // Allow both 200 and 201
                data.responseData!!
            }else{
                throw catchError(data.statusCode.value, null, message = data.messageError?.message)
            }
        }
    }
}