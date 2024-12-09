package com.example.classroom.domain.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey




@Entity
data class LocalChatRoom(
    @PrimaryKey val id: Int,
    val name: String?,
    val isGroup: Boolean,
    val createdAt: String,
    val lastMessage: String? = null // Adding a field to store the last message
)

@Entity(primaryKeys = ["chatRoomId", "userId"])
data class LocalChatRoomUser(
    val chatRoomId: Int,
    val userId: Int,
    val role: String,
    val isActive: Boolean,
    val isTyping: Boolean,
    val lastSeen: String
)

@Entity
data class LocalMessages(
    @PrimaryKey val id: Int,
    val content: String?,
    val senderId: Int,
    val chatRoomId: Int,
    val sentAt: String,
    val isRead: Boolean,
    val messageType: String,
    val fileUrl: String?
)
