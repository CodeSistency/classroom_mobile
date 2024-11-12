package com.example.classroom.data.remote.dto.cloud

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CloudResposeDto(
    @SerialName("status")
    val status: Boolean,
    @SerialName("message")
    val message: String,
    @SerialName("data")
    val data: Data
) {
    @Serializable
    data class Data(
        @SerialName("data")
        val data: String,
        @SerialName("id")
        val id: String,
        @SerialName("fullPath")
        val fullPath: String,
        )
}