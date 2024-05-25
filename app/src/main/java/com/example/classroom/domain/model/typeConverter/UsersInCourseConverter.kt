package com.example.classroom.domain.model.typeConverter

import androidx.room.TypeConverter
import com.example.classroom.domain.model.entity.LocalUser
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.Exception

class UsersInCourseConverter {


    @TypeConverter
    fun from(users: List<LocalUser>?): String {
        return Gson().toJson(users) ?: ""
    }

    @TypeConverter
    fun to(json: String): List<LocalUser>? {
        return try {
            val listType = object : TypeToken<List<LocalUser>>() {}.type

            Gson().fromJson(json, listType)
        } catch (e: Exception){
            null
        }
    }
}