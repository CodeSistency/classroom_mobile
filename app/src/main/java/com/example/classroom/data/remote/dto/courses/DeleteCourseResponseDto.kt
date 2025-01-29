package com.example.classroom.data.remote.dto.courses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeleteCourseResponseDto(
    @SerialName("code")
    val status: Int,
    @SerialName("message")
    val message: String,
    @SerialName("data")
    val data: Data
) {
    @Serializable
    data class Data(
        @SerialName("eliminado")
        val eliminado: Boolean,

    )
}