package com.example.classroom.data.remote.dto.chat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ChatRoomUserResponseDTO(
    @SerialName("code")
    val status: Int,
    @SerialName("message")
    val message: String,
    @SerialName("data")
    val data: Data
) {
    @Serializable
    data class Data(
        val userId: Int,
        val role: String,
        val isActive: Boolean,
        val isTyping: Boolean,
        val lastSeen: String
    )
}