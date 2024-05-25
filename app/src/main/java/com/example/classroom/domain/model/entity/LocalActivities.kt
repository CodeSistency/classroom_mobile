package com.example.classroom.domain.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.classroom.data.remote.dto.activities.ActivityResponseDto
import com.example.classroom.data.remote.dto.activities.GetActivitiesResponseDto
import com.example.classroom.data.remote.dto.login.signIn.SignInResponseDto

@Entity("localActivities_table")
data class LocalActivities(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo("idApi") val idApi: String,
    @ColumnInfo("idCourse") val idCourse: String,
    @ColumnInfo("owner") val owner: String,
    @ColumnInfo("owner_name") val ownerName: String,
    @ColumnInfo("title") val title: String,
    @ColumnInfo("description") val description: String?,
    @ColumnInfo("grade") val grade: Double? = null,
    @ColumnInfo("start_date") val startDate: String,
    @ColumnInfo("end_date") val endDate: String,
    @ColumnInfo("status") val status: Status,
    )
enum class Status{
    LATE,
    OPEN,
    FINISHED
}

fun Status.toInt(status: Status): Int{
    return when(status){
        Status.LATE -> 1
        Status.OPEN -> 1
        Status.FINISHED -> 1
    }
}

fun ActivityResponseDto.toLocal(): LocalActivities {
    return LocalActivities(
        idApi = data.idApi,
        owner = data.owner,
        title = data.title,
        description = data.description,
        ownerName = data.ownerName,
        endDate = data.endDate,
        grade = data.grade,
        idCourse = data.idCourse,
        startDate = data.startDate,
        status = data.status

    )
}

fun GetActivitiesResponseDto.toLocal() : List<LocalActivities>{
    return data.courses.map {
        LocalActivities(
            idApi = it.id,
            description = it.description,
            status = it.status,
            startDate = it.startDate,
            title = it.title,
            endDate = it.endDate,
            grade = it.grade,
            idCourse = it.idCourse,
            ownerName = it.ownerName,
            owner = it.owner,
        )
    }
}