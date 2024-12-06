package com.example.classroom.domain.repository

import com.example.classroom.data.local.db.daos.MessageDao
import com.example.classroom.domain.model.entity.LocalMessages


interface ChatRepository {
    suspend fun saveMessage(message: LocalMessages)
    suspend fun getMessagesByChatRoom(chatRoomId: Int): List<LocalMessages>
    suspend fun clearMessagesForChatRoom(chatRoomId: Int)
}