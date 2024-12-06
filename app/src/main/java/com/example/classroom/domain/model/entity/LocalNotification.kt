package com.example.classroom.domain.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LocalNotification(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String?,
    val message: String?,
    val timestamp: Long,
    val isSeen: Boolean = false
)
