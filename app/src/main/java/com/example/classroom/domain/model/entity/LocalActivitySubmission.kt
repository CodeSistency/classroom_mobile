package com.example.classroom.domain.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.classroom.data.remote.dto.evaluations.evaluationsSent.EvaluationsSentResponseDto
import com.example.classroom.data.remote.dto.evaluations.reviewEvaluationDto.ReviewEvaluationsResponseDto
import com.example.classroom.data.remote.dto.evaluations.sendEvaluationRequestDto.SendEvaluationResponseDto
import com.example.classroom.data.remote.dto.login.signIn.SignInResponseDto


@Entity(tableName = "localActivitySubmission_table")
data class LocalActivitySubmission(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "idAp") val idApi: String,
    @ColumnInfo(name = "activity_id") val activityId: String,
    @ColumnInfo(name = "course_id") val courseId: String,
    @ColumnInfo(name = "student_id") val studentId: String,
    @ColumnInfo(name = "submission_date") val submissionDate: String,
    @ColumnInfo(name = "comment") val comment: String?,
    @ColumnInfo(name = "document_url") val documentUrl: String?, // Link to the submitted document
    @ColumnInfo(name = "grade") val grade: Double = 0.0 // Grade given by the professor
)

fun ReviewEvaluationsResponseDto.toActivitySubmission(): LocalActivitySubmission {
    return LocalActivitySubmission(
        activityId = "",
        grade = 0.0,
        courseId = "",
        id = 0,
        comment = "",
        submissionDate = "",
        documentUrl = "",
        studentId = "",
        idApi = ""

    )
}

fun SendEvaluationResponseDto.toActivitySubmission(idCourse: String): LocalActivitySubmission {
    return LocalActivitySubmission(
        activityId = data.activityId.toString(),
        grade = data.grade,
        courseId = idCourse,
        id = 0,
        comment = data.message,
        submissionDate = data.createDate,
        documentUrl = data.document,
        studentId = data.userId.toString(),
        idApi = data.id.toString()

        )
}

fun EvaluationsSentResponseDto.toActivitiesSubmission(idCourse: String): List<LocalActivitySubmission> {
    return data.map {
            LocalActivitySubmission(
            activityId = it.activityId.toString(),
            grade = it.grade,
                courseId = idCourse,
            id = 0,
            comment = it.message,
            submissionDate = it.createDate,
            documentUrl = it.document,
            studentId = it.userId.toString(),
idApi = it.idApi.toString()
            )
    }
}

