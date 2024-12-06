package com.example.classroom.data.local.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.classroom.domain.model.entity.LocalMessages


@Dao
interface MessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: LocalMessages)

    @Query("SELECT * FROM messages WHERE chat_room_id = :chatRoomId ORDER BY created_at DESC")
    suspend fun getMessagesByChatRoom(chatRoomId: Int): List<LocalMessages>

    @Query("DELETE FROM messages WHERE chat_room_id = :chatRoomId")
    suspend fun clearMessagesForChatRoom(chatRoomId: Int)
}