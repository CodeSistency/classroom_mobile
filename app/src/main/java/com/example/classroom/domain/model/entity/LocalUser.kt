package com.example.classroom.domain.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.classroom.data.remote.dto.courses.GetUsersByCourseResponse
import com.example.classroom.data.remote.dto.login.signIn.SignInResponseDto
import com.example.classroom.data.remote.dto.login.signUp.SignUpResponseDto

@Entity("localUser_table")
data class LocalUser(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo("idApi") val idApi: String,
    @ColumnInfo("name") val name: String,
    @ColumnInfo("lastname") val lastname: String,
    @ColumnInfo("email") val email: String,
    @ColumnInfo("gender") val gender: Gender,
    @ColumnInfo("birthdate") val birthdate: String,
    @ColumnInfo("phone") val phone: String,
    @ColumnInfo("isLogged") val isLogged: Boolean,
    @ColumnInfo( "coursesId") val coursesId: List<Int> = listOf(),
//    @ColumnInfo( "coursesId") val coursesId: String = "[]"
)

enum class Gender(
    name: String,
    id: Int
) {
    Man("Hombre", 1),
    Woman("Mujer", 2),
    Other("Otro", 3)
}

fun gendertoInt(id: Int): Gender {
    return when(id){
        1 -> Gender.Man
        2 -> Gender.Woman
        3 -> Gender.Other
        else -> { Gender.Man }
    }
}

fun gendertoInt(gender: Gender): Int {
    return when(gender){
        Gender.Man -> 1
        Gender.Woman -> 2
        Gender.Other -> 3
    }
}
fun SignInResponseDto.toLoginLocal(): LocalUser {
    return LocalUser(
        idApi = data.userId.toString(),
        name = data.name,
        lastname = data.lastName,
        email = data.email,
        phone = data.phone,
//        birthdate = data.birthdate,
        gender = gendertoInt(data.gender),
        birthdate = "",
        isLogged = true

    )
}

fun SignUpResponseDto.toLoginLocal(): LocalUser {
    return LocalUser(
        idApi = data.userId.toString(),
        name = data.name,
        lastname = data.lastName,
        email = data.email,
        phone = data.phone,
//        birthdate = data.birthdate,
        gender = gendertoInt(data.gender),
        birthdate = "",
        isLogged = false
    )
}

fun GetUsersByCourseResponse.toLocal(): List<LocalUser> {
    return data.users.map {
        LocalUser(
            idApi = it.userId.toString(),
            name = it.user.name,
            lastname = it.user.lastName,
            email = it.user.email,
            phone = it.user.phone,
//        birthdate = data.birthdate,
            gender = gendertoInt(it.user.genderId),
            birthdate = "",
            isLogged = false
        )
    }
}