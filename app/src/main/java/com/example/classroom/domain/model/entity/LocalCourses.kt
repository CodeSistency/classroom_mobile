package com.example.classroom.domain.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.classroom.data.remote.dto.courses.CourseResponseDto
import com.example.classroom.data.remote.dto.courses.GetCoursesResponseDto
import com.example.classroom.domain.model.typeConverter.UsersInCourseConverter

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
    @ColumnInfo("area") val area: Area?,
//    @ColumnInfo("users", defaultValue = "") var users: List<CourseResponseDto.Data.Users>,
//    @TypeConverters(UsersInCourseConverter::class)
//    @ColumnInfo("users", defaultValue = "") var users: List<LocalUser>?,

    )

enum class Area {
    SCIENCE,
    MATH,
    LANGUAGE,
    BIOLOGY,
    NATURE,
    CODING,
    OTHER,
    NO_SELECTED
}

fun Area.toInt(area: Area): Int {
    return when(area){
        Area.SCIENCE -> 1
        Area.MATH -> 1
        Area.LANGUAGE -> 1
        Area.BIOLOGY -> 1
        Area.NATURE -> 1
        Area.CODING -> 1
        Area.OTHER -> 1
        Area.NO_SELECTED -> 1
    }
}

fun CourseResponseDto.toCourseLocal(): LocalCourses {
    return LocalCourses(
        idApi = data.idApi,
        title = data.title,
        subject = data.subject,
        section = data.section,
        ownerName = data.ownerName,
        owner = data.owner,
        description = data.description,
        area = data.area,
//        users = data.users.map {
//            LocalUser(
//                name = it.name,
//                lastname = it.lastname,
//                phone = it.phone,
//                birthdate = it.birthdate,
//                gender = it.gender,
//                idApi = it.idApi,
//                email = it.email
//            )
//        }


    )
}

fun GetCoursesResponseDto.toCoursesLocal(): List<LocalCourses> {
    return data.courses.map {
         LocalCourses(
            idApi = it.id,
            title = it.title,
            subject = it.subject,
            section = it.section,
            ownerName = it.ownerName,
            owner = it.owner,
            description = it.description,
            area = it.area,
//            users = it.users.map {
//                LocalUser(
//                    name = it.name,
//                    lastname = it.lastname,
//                    phone = it.phone,
//                    birthdate = it.birthdate,
//                    gender = it.gender,
//                    idApi = it.idApi,
//                    email = it.email
//                )
//            },
        )
    }
}