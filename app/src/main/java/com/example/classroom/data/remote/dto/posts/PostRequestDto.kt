package com.example.classroom.data.remote.dto.posts

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostRequestDto(
    @SerialName("title")
    val title: String,
    @SerialName("content")
    val content: String,
    @SerialName("courseId")
    val courseId: Int,
    @SerialName("authorId")
    val authorId: Int

)