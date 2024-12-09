package com.example.classroom.data.remote.dto.chat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable



@Serializable
data class MessageResponseDTO(
    @SerialName("code")
    val status: Int,
    @SerialName("message")
    val message: String,
    @SerialName("data")
    val data: Data
) {
    @Serializable
    data class Data(
        val id: Int,
        val content: String?,
        val senderId: Int,
        val chatRoomId: Int,
        val sentAt: String,
        val isRead: Boolean,
        val messageType: String,
        val fileUrl: String?
    )
}