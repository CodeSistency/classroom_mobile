package com.example.classroom.data.repository

import com.example.classroom.data.local.db.daos.MessageDao
import com.example.classroom.domain.model.entity.LocalMessages
import com.example.classroom.domain.repository.ChatRepository


class ChatRepositoryImpl(private val messageDao: MessageDao): ChatRepository {

    override suspend fun saveMessage(message: LocalMessages) {
        messageDao.insertMessage(message)
    }

    override suspend fun getMessagesByChatRoom(chatRoomId: Int): List<LocalMessages> {
        return messageDao.getMessagesByChatRoom(chatRoomId)
    }

    override suspend fun clearMessagesForChatRoom(chatRoomId: Int) {
        messageDao.clearMessagesForChatRoom(chatRoomId)
    }
}