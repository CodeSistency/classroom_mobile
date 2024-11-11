package com.example.classroom.domain.repository

import com.example.classroom.domain.model.entity.LocalPost
import kotlinx.coroutines.flow.Flow

interface PostsRepository {

    suspend fun insertPost(post: LocalPost)

    suspend fun updatePost(post: LocalPost)

    suspend fun deletePost(post: LocalPost)

    fun getPostsByCourse(courseId: String): Flow<List<LocalPost>>

    suspend fun getPostById(postId: Int): LocalPost?
}