package com.example.classroom.domain.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("localCourses_table")
data class LocalCourses(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo("idApi") val idApi: String,
    @ColumnInfo("title") val title: String,
    @ColumnInfo("description") val description: String?,
    @ColumnInfo("owner") val owner: String,
    @ColumnInfo("owner_name") val ownerName: String,
    @ColumnInfo("section") val section: String,
    @ColumnInfo("subject") val subject: String,
    @ColumnInfo("area") val area: Area,
    @ColumnInfo("users", defaultValue = "") var users: List<LocalUser>,

    )

enum class Area {
    SCIENCE,
    MATH,
    LANGUAGE,
    BIOLOGY,
    NATURE,
    CODING,
    OTHER
}