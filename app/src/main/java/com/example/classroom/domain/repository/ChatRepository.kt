package com.example.classroom.domain.repository

import com.example.classroom.common.ResponseGenericAPi
import com.example.classroom.data.local.db.daos.MessageDao
import com.example.classroom.data.remote.dto.chat.ChatRoomDTO
import com.example.classroom.data.remote.dto.chat.MessageDTO
import com.example.classroom.data.remote.dto.chat.TypingStatusDTO
import com.example.classroom.data.remote.dto.chat.UserStatusDTO
import com.example.classroom.domain.model.entity.LocalChatRoom
import com.example.classroom.domain.model.entity.LocalChatRoomUser
import com.example.classroom.domain.model.entity.LocalMessages


interface ChatRepository {
    // Local Database Methods
    suspend fun insertChatRoom(chatRoom: LocalChatRoom)
    suspend fun getChatRooms(userId: Int): List<LocalChatRoom>
    suspend fun insertChatRoomUser(chatRoomUser: LocalChatRoomUser)
    suspend fun getChatRoomUsers(chatRoomId: Int): List<LocalChatRoomUser>
    suspend fun insertMessage(message: LocalMessages)
    suspend fun getMessages(roomId: Int): List<LocalMessages>
    suspend fun updateMessage(message: LocalMessages)
    suspend fun updateMessageReadStatus(roomId: Int, messageId: Int, isRead: Boolean)
    suspend fun updateTypingStatus(roomId: Int, userId: Int, isTyping: Boolean)
    suspend fun updateUserLastSeen(roomId: Int, userId: Int, lastSeen: String)
    suspend fun getLastMessageForChat(chatRoomId: Int): LocalMessages?


    // Remote API Methods
    suspend fun getOrCreatePrivateRoom(userId1: Int, userId2: Int): ResponseGenericAPi<ChatRoomDTO>
    suspend fun createGroupRoom(userId: Int, userIds: List<Int>, roomName: String): ResponseGenericAPi<ChatRoomDTO>
    suspend fun sendMessage(userId: Int, roomId: Int, content: String, messageType: String): ResponseGenericAPi<MessageDTO>
    suspend fun updateTypingStatusRemote(userId: Int, roomId: Int, isTyping: Boolean): ResponseGenericAPi<TypingStatusDTO>
    suspend fun updateUserStatus(userId: Int, isOnline: Boolean): ResponseGenericAPi<UserStatusDTO>
    suspend fun getMessagesRemote(roomId: Int): ResponseGenericAPi<List<MessageDTO>>


}