package com.example.classroom.presentation.screens.chats.states

import com.example.classroom.domain.model.entity.LocalMessages

data class Message(
    val id: Int,
    val content: String,
    val senderId: Int,
    val chatRoomId: Int,
    val createdAt: String
)

