package com.example.classroom.data.repository

import com.example.classroom.data.local.db.AppDao
import com.example.classroom.data.remote.ApiService
import com.example.classroom.data.remote.dto.activities.ActivityRequestDto
import com.example.classroom.data.remote.dto.activities.ActivityResponseDto
import com.example.classroom.data.remote.dto.activities.GetActivitiesResponseDto
import com.example.classroom.domain.model.entity.LocalActivities
import com.example.classroom.domain.repository.ActivitiesRepository
import kotlinx.coroutines.flow.Flow
import com.example.classroom.common.ResponseGenericAPi

class ActivitiesRepositoryImpl(
    private val apiService: ApiService,
    private val dao: AppDao
): ActivitiesRepository {
    override suspend fun insertActivityRemote(activity: ActivityRequestDto) : ResponseGenericAPi<ActivityResponseDto> {
        return apiService.insertActivityRemote(activity)
    }
    override suspend fun updateActivityRemote(activity: ActivityRequestDto, id: String) : ResponseGenericAPi<ActivityResponseDto> {
        return apiService.updateActivityRemote(activity, id)
    }
    override suspend fun deleteActivityRemote(id: String): ResponseGenericAPi<Boolean> {
        return apiService.deleteActivityRemote(id)
    }
    override suspend fun getActivitiesRemote(id: String): ResponseGenericAPi<GetActivitiesResponseDto> {
        return apiService.getActivitiesRemote(id)
    }

    override suspend fun getActivitiesByCourseRemote(id: String): ResponseGenericAPi<GetActivitiesResponseDto> {
        return apiService.getActivitiesByCourseRemote(id)
    }

    override suspend fun getActivitiesWithFlowRemote(): Flow<List<LocalActivities>> {
        return apiService.getActivitiesWithFlowRemote()
    }
    override suspend fun insertActivity(localActivity: LocalActivities) {
        return dao.insertLocalActivity(localActivity)
    }

    override suspend fun insertAllActivities(activities: List<LocalActivities>) {
        return dao.insertAllLocalActivities(activities)
    }

    override suspend fun updateActivity(localActivity: LocalActivities) {
        return dao.insertLocalActivity(localActivity)
    }
    override suspend fun deleteActivity(id: String) {
        return dao.deleteLocalActivityById(id)
    }
    override suspend fun getActivities(): List<LocalActivities> {
        return dao.getActivitiesInfo()
    }
    override suspend fun getActivitiesWithFlow(): Flow<List<LocalActivities>> {
        return dao.getActivitiesInfoWithFlow()
    }
    override suspend fun getActivityWithFlowById(id: String): Flow<LocalActivities> {
        return dao.getActivityById(id)
    }
    override suspend fun getActivitiesWithFlowByCourse(id: String): Flow<List<LocalActivities>> {
        return dao.getActivitiesInfoWithFlowByCourse(id)
    }

}