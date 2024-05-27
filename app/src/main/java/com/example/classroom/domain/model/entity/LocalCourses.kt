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
    @ColumnInfo("token") val token: String,
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

fun areatoInt(area: Area): Int {
    return when(area){
        Area.SCIENCE -> 1
        Area.MATH -> 2
        Area.LANGUAGE -> 3
        Area.BIOLOGY -> 4
        Area.NATURE -> 5
        Area.CODING -> 6
        Area.OTHER -> 7
        Area.NO_SELECTED -> 8
    }
}

fun intToArea(id: Int): Area {
    return when(id) {
        1 -> Area.SCIENCE
        2 -> Area.MATH
        3 -> Area.LANGUAGE
        4 -> Area.BIOLOGY
        5 -> Area.NATURE
        6 -> Area.CODING
        7 -> Area.OTHER
        8 -> Area.NO_SELECTED
        else -> Area.NO_SELECTED // default case for unknown values
    }
}

fun CourseResponseDto.toCourseLocal(): LocalCourses {
    return LocalCourses(
        idApi = data.idApi.toString(),
        title = data.title,
        subject = data.subject,
        section = data.section,
        ownerName = data.ownerName,
        owner = data.owner.toString(),
        description = data.description,
        area = intToArea(data.areaId),
        token = data.token
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
    return data.map {
         LocalCourses(
            idApi = it.id.toString(),
            title = it.title,
            subject = it.subject,
            section = it.section,
            ownerName = it.ownerName,
            owner = it.owner.toString(),
             area = intToArea(it.areaId),
             token = it.token,
             description = it.description
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