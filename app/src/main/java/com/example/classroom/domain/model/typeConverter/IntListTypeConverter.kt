package com.example.classroom.domain.model.typeConverter

import androidx.room.TypeConverter
import com.google.gson.Gson
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class IntListTypeConverter {

    @TypeConverter
    fun from(input: List<Int>): String {
        return Gson().toJson(input)
    }

    @TypeConverter
    fun to (input: String): List<Int> {
       return Gson().fromJson(input, Array<Int>::class.java).toList()
    }
}


