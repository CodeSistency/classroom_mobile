package com.example.classroom.data.repository

import com.example.classroom.common.ResponseGenericAPi
import com.example.classroom.data.local.db.AppDao
import com.example.classroom.data.remote.ApiService
import com.example.classroom.data.remote.dto.cloud.CloudResposeDto
import com.example.classroom.domain.repository.CloudRepository
import com.example.classroom.domain.repository.CoursesRepository
import java.io.File

class CloudRepositoryImpl(
    private val apiService: ApiService,
    private val dao: AppDao
): CloudRepository {
    override suspend fun uploadFile(file: File): ResponseGenericAPi<CloudResposeDto> {
        return apiService.uploadFile(file)
    }
}