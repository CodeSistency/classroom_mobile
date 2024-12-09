package com.example.classroom.data.repository

import com.example.classroom.common.ResponseGenericAPi
import com.example.classroom.data.local.db.daos.MessageDao
import com.example.classroom.data.remote.ApiService
import com.example.classroom.data.remote.dto.chat.ChatRoomDTO
import com.example.classroom.data.remote.dto.chat.MessageDTO
import com.example.classroom.data.remote.dto.chat.TypingStatusDTO
import com.example.classroom.data.remote.dto.chat.UserStatusDTO
import com.example.classroom.domain.model.entity.LocalChatRoom
import com.example.classroom.domain.model.entity.LocalChatRoomUser
import com.example.classroom.domain.model.entity.LocalMessages
import com.example.classroom.domain.repository.ChatRepository
import com.example.classroom.domain.services.ChatWebSocket


class ChatRepositoryImpl(
    private val chatDao: MessageDao,
    private val apiService: ApiService,
): ChatRepository {

    // Local Database Methods
    override suspend fun insertChatRoom(chatRoom: LocalChatRoom) {
        chatDao.insertChatRoom(chatRoom)
    }

    override suspend fun getChatRooms(userId: Int): List<LocalChatRoom> {
        return chatDao.getChatRooms(userId)
    }

    override suspend fun insertChatRoomUser(chatRoomUser: LocalChatRoomUser) {
        chatDao.insertChatRoomUser(chatRoomUser)
    }

    override suspend fun getChatRoomUsers(chatRoomId: Int): List<LocalChatRoomUser> {
        return chatDao.getChatRoomUsers(chatRoomId)
    }

    override suspend fun insertMessage(message: LocalMessages) {
        chatDao.insertMessage(message)
    }

    override suspend fun getMessages(roomId: Int): List<LocalMessages> {
        return chatDao.getMessages(roomId)
    }

    override suspend fun updateMessage(message: LocalMessages) {
        chatDao.updateMessage(message)
    }

    override suspend fun updateMessageReadStatus(roomId: Int, messageId: Int, isRead: Boolean) {
        chatDao.updateMessageReadStatus(roomId, messageId, isRead)
    }

    override suspend fun updateTypingStatus(roomId: Int, userId: Int, isTyping: Boolean) {
        chatDao.updateTypingStatus(roomId, userId, isTyping)
    }

    override suspend fun updateUserLastSeen(roomId: Int, userId: Int, lastSeen: String) {
        chatDao.updateUserLastSeen(roomId, userId, lastSeen)
    }


    override suspend fun getLastMessageForChat(chatRoomId: Int): LocalMessages? {
        // Fetch the last message from the database
        return chatDao.getLastMessageForChat(chatRoomId)
    }

    // Remote API Methods
    override suspend fun getOrCreatePrivateRoom(userId1: Int, userId2: Int): ResponseGenericAPi<ChatRoomDTO> {
        return apiService.getOrCreatePrivateRoom(userId1, userId2)
    }

    override suspend fun createGroupRoom(userId: Int, userIds: List<Int>, roomName: String): ResponseGenericAPi<ChatRoomDTO> {
        return apiService.createGroupRoom(userId, userIds, roomName)
    }

    override suspend fun sendMessage(userId: Int, roomId: Int, content: String, messageType: String): ResponseGenericAPi<MessageDTO> {
        return apiService.sendMessage(userId, roomId, content, messageType)
    }

    override suspend fun updateTypingStatusRemote(userId: Int, roomId: Int, isTyping: Boolean): ResponseGenericAPi<TypingStatusDTO> {
        return apiService.updateTypingStatus(userId, roomId, isTyping)
    }

    override suspend fun updateUserStatus(userId: Int, isOnline: Boolean): ResponseGenericAPi<UserStatusDTO> {
        return apiService.updateUserStatus(userId, isOnline)
    }

    override suspend fun getMessagesRemote(roomId: Int): ResponseGenericAPi<List<MessageDTO>> {
        return apiService.getMessages(roomId)
    }



}