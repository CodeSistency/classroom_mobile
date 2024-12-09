package com.example.classroom.data.remote.dto.chat

import com.example.classroom.data.remote.dto.activities.GetActivitiesResponseDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ChatRoomResponseDTO(
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
        val name: String?,
        val isGroup: Boolean,
        val createdAt: String,
        val users: List<ChatRoomUserResponseDTO>
    )
}


@Serializable
data class ChatRoomDTO(
    val id: Int,
    val name: String?,
    val isGroup: Boolean,
    val createdAt: String
)

@Serializable
data class MessageDTO(
    val id: Int,
    val content: String?,
    val senderId: Int,
    val chatRoomId: Int,
    val sentAt: String,
    val isRead: Boolean,
    val messageType: String,
    val fileUrl: String?
)

@Serializable
data class TypingStatusDTO(
    val userId: Int,
    val isTyping: Boolean
)

@Serializable
data class UserStatusDTO(
    val userId: Int,
    val isOnline: Boolean
)