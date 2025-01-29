package com.example.classroom.data.repository

import com.example.classroom.common.ResponseGenericAPi
import com.example.classroom.data.local.db.daos.LocalPostDao
import com.example.classroom.data.remote.ApiService
import com.example.classroom.data.remote.dto.posts.GetPostsResponseDto
import com.example.classroom.data.remote.dto.posts.PostRequestDto
import com.example.classroom.data.remote.dto.posts.PostResponseDto
import com.example.classroom.domain.model.entity.LocalPost
import com.example.classroom.domain.repository.PostsRepository
import kotlinx.coroutines.flow.Flow

class PostsRepositoryImpl(private val localPostDao: LocalPostDao, private val apiService: ApiService,
): PostsRepository {

    override suspend fun insertPost(post: LocalPost) {
        return localPostDao.insertPost(post)
    }

    override suspend fun updatePost(post: LocalPost) {
        return localPostDao.updatePost(post)
    }

    override suspend fun deletePost(post: LocalPost) {
        return localPostDao.deletePost(post)
    }

    override suspend fun deletePostById(idApi: String) {
        return localPostDao.deletePostsById(idApi)
    }

    override fun getPostsByCourse(courseId: String): Flow<List<LocalPost>> {
        return localPostDao.getPostsByCourse(courseId)
    }

    override suspend fun getPostById(postId: Int): LocalPost? {
        return localPostDao.getPostById(postId)
    }

    override suspend fun updateListPosts(courseId: String, posts: List<LocalPost>) {
        return localPostDao.updatePostsByCourse(courseId, posts)
    }

    override suspend fun getPostByCourseRemote(id: String): ResponseGenericAPi<GetPostsResponseDto> {
        return apiService.getPostByCourseRemote(id)
    }

    override suspend fun createPostRemote(body: PostRequestDto): ResponseGenericAPi<PostResponseDto> {
        return apiService.createPostRemote(body)
    }

    override suspend fun updatePostRemote(body: PostRequestDto): ResponseGenericAPi<PostResponseDto> {
        return apiService.updatePostRemote(body)
    }

    override suspend fun deletePostRemote(id: String): ResponseGenericAPi<PostResponseDto> {
       return apiService.deletePostRemote(id)
    }
}