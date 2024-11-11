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
    @ColumnInfo(name = "idApi") val idApi: String,
    @ColumnInfo(name = "activity_id") val activityId: String,
    @ColumnInfo(name = "course_id") val courseId: String,
    @ColumnInfo(name = "student_id") val studentId: String,
    @ColumnInfo(name = "submission_date") val submissionDate: String,
    @ColumnInfo(name = "comment") val comment: String?,
    @ColumnInfo(name = "document_url") val documentUrl: String?, // Link to the submitted document
    @ColumnInfo(name = "grade") val grade: Float = 0f // Grade given by the professor
)

fun ReviewEvaluationsResponseDto.toActivitySubmission(): LocalActivitySubmission {
    return LocalActivitySubmission(
        idApi = "",
        activityId = "",
        grade = 0f,
        courseId = "",
        id = 0,
        comment = "",
        submissionDate = "",
        documentUrl = "",
        studentId = "",

    )
}

fun SendEvaluationResponseDto.toActivitySubmission(): LocalActivitySubmission {
    return LocalActivitySubmission(
        idApi = "",
        activityId = "",
        grade = 0f,
        courseId = "",
        id = 0,
        comment = "",
        submissionDate = "",
        documentUrl = "",
        studentId = "",

        )
}

fun EvaluationsSentResponseDto.toActivitiesSubmission(): List<LocalActivitySubmission> {
    return data.map {
        LocalActivitySubmission(
            idApi = "",
            activityId = "",
            grade = 0f,
            courseId = "",
            id = 0,
            comment = "",
            submissionDate = "",
            documentUrl = "",
            studentId = "",

            )
    }
}

