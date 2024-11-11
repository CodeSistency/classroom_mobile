package com.example.classroom.domain.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "localStudentEvaluation_table")
data class LocalStudentEvaluation(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "activity_id") val activityId: Int,
    @ColumnInfo(name = "student_id") val studentId: Int,
    @ColumnInfo(name = "evaluation_date") val evaluationDate: String,
    @ColumnInfo(name = "score") val score: Float,
    @ColumnInfo(name = "comments") val comments: String? // Optional comments on evaluation
)