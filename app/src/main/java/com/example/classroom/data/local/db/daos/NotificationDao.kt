package com.example.classroom.data.local.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.classroom.domain.model.entity.LocalNotification
import kotlinx.coroutines.flow.Flow


@Dao
interface NotificationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(notification: LocalNotification)

    @Query("SELECT * FROM LocalNotification ORDER BY timestamp DESC")
    fun getAllNotifications(): Flow<List<LocalNotification>>

    @Query("UPDATE LocalNotification SET isSeen = 1 WHERE id IN (:ids)")
    suspend fun markAsSeen(ids: List<Int>)

    @Query("SELECT COUNT(*) FROM LocalNotification WHERE isSeen = 0")
    fun getUnseenCount(): Flow<Int>
}