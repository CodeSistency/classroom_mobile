package com.example.classroom.data.remote.dto.posts

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetPostsResponseDto(
    @SerialName("code")
    val status: Int,
    @SerialName("message")
    val message: String,
    @SerialName("data")
    val data: List<PostData>
)

@Serializable
data class PostData(
    @SerialName("id")
    val id: Int,
    @SerialName("courseId")
    val courseId: Int,
    @SerialName("authorId")
    val authorId: Int,
    @SerialName("title")
    val title: String,
    @SerialName("content")
    val content: String,
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("activityId")
    val activityId: Int? // Nullable because activityId can be null
)