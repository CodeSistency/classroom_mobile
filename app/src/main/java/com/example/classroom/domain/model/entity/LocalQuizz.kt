package com.example.classroom.domain.model.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation


@Entity
data class QuizEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val activityId: Int,
    val title: String
)

@Entity(
    foreignKeys = [ForeignKey(
        entity = QuizEntity::class,
        parentColumns = ["id"],
        childColumns = ["quizId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class QuestionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val quizId: Int,
    val text: String,
    val correctAnswer: Int
)

@Entity(
    foreignKeys = [ForeignKey(
        entity = QuestionEntity::class,
        parentColumns = ["id"],
        childColumns = ["questionId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class OptionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val questionId: Int,
    val text: String
)

@Entity
data class AnswerEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val questionId: Int,
    val selectedOptionId: Int
)


