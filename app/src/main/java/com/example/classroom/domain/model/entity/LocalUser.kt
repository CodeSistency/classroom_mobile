package com.example.classroom.domain.model.entity

import android.util.Log
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
    @ColumnInfo("firebaseToken") val firebaseToken: String = "",
    @ColumnInfo("email") val email: String,
    @ColumnInfo("gender") val gender: Gender,
    @ColumnInfo("birthdate") val birthdate: String,
    @ColumnInfo("phone") val phone: String,
//    @ColumnInfo( "coursesId") val coursesId: String = "[]"
)

enum class Gender(
    val displayName: String,
    val id: Int
) {
    Man("Hombre", 1),
    Woman("Mujer", 2),
    Other("Otro", 3)
}

enum class Role(val id: Int, val displayName: String) {
    ADMIN(1, "Administrador"),
    TEACHER(2, "Profesor"),
    STUDENT(3, "Estudiante"),
    GUEST(4, "Invitado")
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
    Log.e("signindata", data.toString())

    return LocalUser(
        idApi = data.userId.toString(),
        name = data.name,
        lastname = data.lastName,
        email = data.email,
        phone = data.phone,
        gender = gendertoInt(data.gender),
        birthdate = data.creation,
        firebaseToken = data.firebaseToken ?: ""

    )
}

fun SignUpResponseDto.toLoginLocal(): LocalUser {
    Log.e("signupdata", data.toString())
    return LocalUser(
        idApi = data.userId.toString(),
        name = data.name,
        lastname = data.lastName,
        email = data.email,
        phone = data.phone,
        gender = gendertoInt(data.gender),
        birthdate = data.creation,
        firebaseToken = data.firebaseToken ?: ""
    )
}

fun GetUsersByCourseResponse.toLocal(): List<LocalStudents> {
    return data.users.map {
        LocalStudents(
            idApi = it.userId.toString(),
            name = it.user.name,
            lastname = it.user.lastName,
            email = it.user.email,
            phone = it.user.phone,
            courseId = it.courseId.toString(),
            id = it.id,
            gender = Gender.Man,
            birthdate = it.user.createDate,
            firebaseToken = it.user.firebaseToken ?: ""
//        birthdate = data.birthdate,
//            gender = gendertoInt(it.user.genderId),
//            birthdate = "",
        )
    }
}