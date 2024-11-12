package com.example.classroom.domain.repository

import com.example.classroom.common.ResponseGenericAPi
import com.example.classroom.data.remote.dto.cloud.CloudResposeDto
import io.ktor.client.statement.HttpResponse
import java.io.File

interface CloudRepository {

    suspend fun uploadFile(file: File): ResponseGenericAPi<CloudResposeDto>

}