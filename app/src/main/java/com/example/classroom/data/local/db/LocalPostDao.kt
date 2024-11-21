package com.example.classroom.data.local.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosts(posts: List<LocalPost>)

    @Query("DELETE FROM localPost_table WHERE course_id = :courseId")
    suspend fun deletePostsByCourse(courseId: String)

    @Transaction
    suspend fun updatePostsByCourse(courseId: String, posts: List<LocalPost>) {
        // Delete old posts for the course
        deletePostsByCourse(courseId)
        // Insert the new list of posts
        insertPosts(posts)
    }
}
