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
    @ColumnInfo("title") val title: String,
    @ColumnInfo("description") val description: String?,
    @ColumnInfo("grade") val grade: Int = 0,
    @ColumnInfo("start_date") val startDate: String,
    @ColumnInfo("end_date") val endDate: String,
    @ColumnInfo("status") val status: Status,
    )
enum class Status{
    LATE,
    OPEN,
    FINISHED
}

fun statusToInt(status: Status): Int{
    return when(status){
        Status.LATE -> 1
        Status.OPEN -> 1
        Status.FINISHED -> 1
    }
}

fun intToStatus(id: Int): Status{
    return when(id){
        1 -> Status.LATE
        2 -> Status.OPEN
        3 -> Status.FINISHED

        else -> {Status.OPEN}
    }
}

fun ActivityResponseDto.toLocal(): LocalActivities {
    return LocalActivities(
        idApi = data.idApi.toString(),
        title = data.title,
        description = data.description,
        endDate = data.endDate ?: "",
        grade = data.grade,
        idCourse = data.idCourse.toString(),
        startDate = data.startDate ?: "",
        status = intToStatus(data.status)

    )
}

fun GetActivitiesResponseDto.toLocal() : List<LocalActivities>{
    return data.map {
        LocalActivities(
            idApi = it.idApi.toString(),
            description = it.description,
            status = intToStatus(it.status),
            startDate = it.startDate,
            title = it.title,
            endDate = it.endDate,
            grade = it.grade,
            idCourse = it.idCourse.toString(),

        )
    }
}