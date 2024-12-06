package com.example.classroom.domain.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "messages")
data class LocalMessages(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "content") val content: String,
    @ColumnInfo(name = "sender_id") val senderId: Int,
    @ColumnInfo(name = "chat_room_id") val chatRoomId: Int,
    @ColumnInfo(name = "created_at") val createdAt: String
)