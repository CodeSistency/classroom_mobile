package com.example.classroom.domain.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("localStudents_table")
data class LocalStudents(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo("idApi") val idApi: String,
    @ColumnInfo("courseId") val courseId: String,
    @ColumnInfo("name") val name: String,
    @ColumnInfo("lastname") val lastname: String,
    @ColumnInfo("email") val email: String,
    @ColumnInfo("gender") val gender: Gender,
    @ColumnInfo("birthdate") val birthdate: String,
    @ColumnInfo("phone") val phone: String,
//    @ColumnInfo("isLogged") val isLogged: Boolean,
//    @ColumnInfo( "coursesId") val coursesId: List<Int> = listOf(),
//    @ColumnInfo( "coursesId") val coursesId: String = "[]"
)
//data class LocalStudents()
