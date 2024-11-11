package com.example.classroom.domain.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity("localPost_table")
data class LocalPost(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo("idApi") val idApi: String,
    @ColumnInfo("title") val title: String,
    @ColumnInfo("content") val content: String,
    @ColumnInfo("createdAt") val createdAt: String,
    @ColumnInfo("course_id") val courseId: String,
//    @ColumnInfo("activity_id") val activityId: Int?,
    @ColumnInfo("author_id") val authorId: String
)

