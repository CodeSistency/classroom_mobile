package com.example.classroom.domain.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.classroom.data.remote.dto.login.signIn.SignInResponseDto
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
)

enum class Gender {
    Man,
    Woman,
    Other
}
fun SignInResponseDto.toLoginLocal(): LocalUser {
    return LocalUser(
        idApi = data.userId,
        name = data.name,
        lastname = data.lastame,
        email = data.email,
        phone = data.phone,
        birthdate = data.birthdate,
        gender = data.gender

    )
}