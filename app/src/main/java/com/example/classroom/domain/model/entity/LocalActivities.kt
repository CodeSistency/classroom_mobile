package com.example.classroom.domain.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.classroom.data.remote.dto.activities.ActivityResponseDto
import com.example.classroom.data.remote.dto.activities.GetActivitiesResponseDto
import com.example.classroom.data.remote.dto.activities.GetActivitiesWithQuizzResponseDto
import com.example.classroom.data.remote.dto.login.signIn.SignInResponseDto
import com.example.classroom.data.remote.dto.quizz.CreateQuizzResponseDto

@Entity("localActivities_table")
data class LocalActivities(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo("idApi") val idApi: String,
    @ColumnInfo("idCourse") val idCourse: String,
    @ColumnInfo("title") val title: String,
    @ColumnInfo("description") val description: String?,
    @ColumnInfo("grade") val grade: Double = 0.0,
    @ColumnInfo("start_date") val startDate: String,
    @ColumnInfo("end_date") val endDate: String,
    @ColumnInfo("status") val status: Status,
    @ColumnInfo("isQuizz") val isQuizz: Boolean = false,
    @ColumnInfo("quizzId") val quizzId: String? = null,

    )

enum class Status(val id: Int, val displayName: String) {
    NO_SELECTED(0, "No seleccionado"),
    OPEN(1, "Abierta"),
    LATE(2, "Retrasado"),
    FINISHED(3, "Finalizado");
    companion object {
        fun fromId(id: Int): Status = Status.values().find { it.id == id } ?: Status.NO_SELECTED
    }
}
//fun statusToInt(status: Status): Int{
//    return when(status){
//        Status.LATE -> 1
//        Status.OPEN -> 1
//        Status.FINISHED -> 1
//    }
//}
//
//fun intToStatus(id: Int): Status{
//    return when(id){
//        1 -> Status.LATE
//        2 -> Status.OPEN
//        3 -> Status.FINISHED
//
//        else -> {Status.OPEN}
//    }
//}

fun ActivityResponseDto.toLocal(): LocalActivities {
    return LocalActivities(
        idApi = data.idApi.toString(),
        title = data.title,
        description = data.description,
        endDate = data.endDate ?: "",
        grade = data.grade,
        idCourse = data.idCourse.toString(),
        startDate = data.startDate ?: "",
        status = Status.fromId(data.status)

    )
}

fun GetActivitiesResponseDto.toLocal() : List<LocalActivities>{
    return data.map {
        LocalActivities(
            idApi = it.idApi.toString(),
            description = it.description,
            status = Status.fromId(it.status),
            startDate = it.startDate,
            title = it.title,
            endDate = it.endDate,
            grade = it.grade,
            idCourse = it.idCourse.toString(),
        )
    }
}

fun GetActivitiesWithQuizzResponseDto.toLocal() : List<LocalActivities>{
    return data.map {
        LocalActivities(
            idApi = it.idApi.toString(),
            description = it.description,
            status = Status.fromId(it.status),
            startDate = it.startDate,
            title = it.title,
            endDate = it.endDate,
            grade = it.grade,
            idCourse = it.idCourse.toString(),
        )
    }
}

fun GetActivitiesWithQuizzResponseDto.Activity.toLocalActivity(): LocalActivities {
    return LocalActivities(
        idApi = this.idApi.toString(),
        idCourse = this.idCourse.toString(),
        title = this.title,
        description = this.description,
        grade = this.grade,
        startDate = this.startDate,
        endDate = this.endDate,
        quizzId = this.quizzId.toString(),
        isQuizz = this.isQuizz,
        status = Status.fromId(this.status),
    )
}


fun CreateQuizzResponseDto.toLocalActivities(idCourse: String): LocalActivities {
    val activity = this.data.activity
    return LocalActivities(
        idApi = activity.id.toString(),
        idCourse = idCourse,
        title = activity.title,
        description = activity.description,
        grade = activity.grade,
        startDate = activity.startDate,
        endDate = activity.endDate,
        status = Status.OPEN,
        isQuizz = activity.isQuizz,
        quizzId = this.data.quizzId.toString()
    )
}