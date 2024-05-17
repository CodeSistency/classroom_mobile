package com.example.classroom.domain.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

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