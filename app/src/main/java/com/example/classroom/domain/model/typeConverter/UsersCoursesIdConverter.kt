package com.example.classroom.domain.model.typeConverter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class UsersCoursesIdConverter {
    @TypeConverter
    fun fromString(value: String?): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return value?.let { Gson().fromJson(it, listType) } ?: emptyList()
    }

    @TypeConverter
    fun fromList(list: List<String>): String {
        return Gson().toJson(list)
    }

}