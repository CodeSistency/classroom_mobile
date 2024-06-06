package com.example.classroom.domain.repository

import com.example.classroom.data.remote.dto.activities.ActivityRequestDto
import com.example.classroom.data.remote.dto.activities.ActivityResponseDto
import com.example.classroom.data.remote.dto.activities.GetActivitiesResponseDto
import com.example.classroom.domain.model.entity.LocalActivities
import kotlinx.coroutines.flow.Flow
import com.example.classroom.common.ResponseGenericAPi

interface ActivitiesRepository {
    //Remote Methods
    suspend fun insertActivityRemote(activity: ActivityRequestDto): ResponseGenericAPi<ActivityResponseDto>
    suspend fun updateActivityRemote(activity: ActivityRequestDto, id: String) : ResponseGenericAPi<ActivityResponseDto>
    suspend fun deleteActivityRemote(id: String) : ResponseGenericAPi<Boolean>
    suspend fun getActivitiesRemote(id: String) : ResponseGenericAPi<GetActivitiesResponseDto>
    suspend fun getActivitiesByCourseRemote(id: String) : ResponseGenericAPi<GetActivitiesResponseDto>

    suspend fun getActivitiesByUserRemote(id: String) : ResponseGenericAPi<GetActivitiesResponseDto>

    suspend fun getActivitiesWithFlowRemote() : Flow<List<LocalActivities>>

    //Local Methods
    suspend fun insertActivity(localActivity: LocalActivities)

    suspend fun insertAllActivities(activities: List<LocalActivities>)
    suspend fun updateActivity(localActivity: LocalActivities)
    suspend fun deleteActivity(id: String)
    suspend fun getActivities() : List<LocalActivities>
    suspend fun getActivitiesWithFlow() : Flow<List<LocalActivities>>
    suspend fun getActivityWithFlowById(id: String) : Flow<LocalActivities?>
    suspend fun getActivitiesWithFlowByCourse(id: String) : Flow<List<LocalActivities>>
}