package com.example.classroom.data.repository

import com.example.classroom.data.local.db.LocalPostDao
import com.example.classroom.data.remote.ApiService
import com.example.classroom.domain.model.entity.LocalPost
import com.example.classroom.domain.repository.PostsRepository
import kotlinx.coroutines.flow.Flow

class PostsRepositoryImpl(private val localPostDao: LocalPostDao, private val apiService: ApiService,
): PostsRepository {

    override suspend fun insertPost(post: LocalPost) {
        localPostDao.insertPost(post)
    }

    override suspend fun updatePost(post: LocalPost) {
        localPostDao.updatePost(post)
    }

    override suspend fun deletePost(post: LocalPost) {
        localPostDao.deletePost(post)
    }

    override fun getPostsByCourse(courseId: String): Flow<List<LocalPost>> {
        return localPostDao.getPostsByCourse(courseId)
    }

    override suspend fun getPostById(postId: Int): LocalPost? {
        return localPostDao.getPostById(postId)
    }
}