package com.example.classroom.data.local.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.classroom.domain.model.entity.LocalChatRoom
import com.example.classroom.domain.model.entity.LocalChatRoomUser
import com.example.classroom.domain.model.entity.LocalMessages


@Dao
interface MessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatRoom(chatRoom: LocalChatRoom)

    @Query("SELECT * FROM LocalChatRoom WHERE id = :userId")
    suspend fun getChatRooms(userId: Int): List<LocalChatRoom>

    // Chat Room User Methods
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatRoomUser(chatRoomUser: LocalChatRoomUser)

    @Query("SELECT * FROM LocalChatRoomUser WHERE chatRoomId = :chatRoomId")
    suspend fun getChatRoomUsers(chatRoomId: Int): List<LocalChatRoomUser>

    @Update
    suspend fun updateChatRoomUser(chatRoomUser: LocalChatRoomUser)

    // Message Methods
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: LocalMessages)

    @Query("SELECT * FROM LocalMessages WHERE chatRoomId = :roomId")
    suspend fun getMessages(roomId: Int): List<LocalMessages>

    @Update
    suspend fun updateMessage(message: LocalMessages)

    @Query("UPDATE LocalMessages SET isRead = :isRead WHERE chatRoomId = :roomId AND id = :messageId")
    suspend fun updateMessageReadStatus(roomId: Int, messageId: Int, isRead: Boolean)

    @Query("UPDATE LocalChatRoomUser SET isTyping = :isTyping WHERE chatRoomId = :roomId AND userId = :userId")
    suspend fun updateTypingStatus(roomId: Int, userId: Int, isTyping: Boolean)

    @Query("UPDATE LocalChatRoomUser SET lastSeen = :lastSeen WHERE chatRoomId = :roomId AND userId = :userId")
    suspend fun updateUserLastSeen(roomId: Int, userId: Int, lastSeen: String)

    @Query("SELECT * FROM LocalMessages WHERE chatRoomId = :chatRoomId ORDER BY sentAt DESC LIMIT 1")
    suspend fun getLastMessageForChat(chatRoomId: Int): LocalMessages?
}