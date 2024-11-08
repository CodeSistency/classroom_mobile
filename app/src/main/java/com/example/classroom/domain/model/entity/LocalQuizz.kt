package com.example.classroom.domain.model.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation


@Entity(tableName = "quizz")
data class QuizzEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "activity_id") val activityId: Int,
    @ColumnInfo(name = "course_id") val courseId: Int
)

@Entity(tableName = "questions")
data class QuestionsEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "quizz_id") val quizzId: Int,
    @ColumnInfo(name = "course_id") val courseId: Int,
    @ColumnInfo(name = "text") val text: String,
    @ColumnInfo(name = "answer") val answer: Int
)

data class QuizzWithQuestions(
    @Embedded val quizz: QuizzEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "quizz_id"
    )
    val questions: List<QuestionsEntity>
)

