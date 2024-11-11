package com.example.classroom.data.local.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.classroom.domain.model.entity.LocalPost
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalPostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: LocalPost)

    @Update
    suspend fun updatePost(post: LocalPost)

    @Delete
    suspend fun deletePost(post: LocalPost)

    @Query("SELECT * FROM localPost_table WHERE course_id = :courseId")
    fun getPostsByCourse(courseId: String): Flow<List<LocalPost>>

    @Query("SELECT * FROM localPost_table WHERE id = :postId")
    suspend fun getPostById(postId: Int): LocalPost?
}
