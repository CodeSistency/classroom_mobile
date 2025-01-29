package com.example.classroom.domain.repository

import com.example.classroom.common.ResponseGenericAPi
import com.example.classroom.data.remote.dto.courses.GetCoursesResponseDto
import com.example.classroom.data.remote.dto.posts.GetPostsResponseDto
import com.example.classroom.data.remote.dto.posts.PostRequestDto
import com.example.classroom.data.remote.dto.posts.PostResponseDto
import com.example.classroom.domain.model.entity.LocalPost
import kotlinx.coroutines.flow.Flow

interface PostsRepository {

    suspend fun insertPost(post: LocalPost)

    suspend fun updatePost(post: LocalPost)

    suspend fun deletePost(post: LocalPost)

    suspend fun deletePostById(idApi: String)


    fun getPostsByCourse(courseId: String): Flow<List<LocalPost>>

    suspend fun getPostById(postId: Int): LocalPost?

    suspend fun updateListPosts(courseId: String, posts: List<LocalPost>)

    //Remote

    suspend fun getPostByCourseRemote(id: String): ResponseGenericAPi<GetPostsResponseDto>

    suspend fun createPostRemote(body: PostRequestDto): ResponseGenericAPi<PostResponseDto>

    suspend fun updatePostRemote(body: PostRequestDto): ResponseGenericAPi<PostResponseDto>

    suspend fun deletePostRemote(id: String): ResponseGenericAPi<PostResponseDto>
}