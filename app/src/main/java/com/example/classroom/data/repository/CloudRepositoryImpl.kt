package com.example.classroom.data.repository

import android.content.Context
import android.net.Uri
import com.example.classroom.common.ResponseGenericAPi
import com.example.classroom.data.local.db.daos.AppDao
import com.example.classroom.data.remote.ApiService
import com.example.classroom.data.remote.dto.cloud.CloudResposeDto
import com.example.classroom.domain.repository.CloudRepository

class CloudRepositoryImpl(
    private val apiService: ApiService,
    private val dao: AppDao
): CloudRepository {
    override suspend fun uploadFile(fileUri: Uri, context: Context): ResponseGenericAPi<CloudResposeDto> {
        return apiService.uploadFile(fileUri, context)
    }
}