package com.example.classroom.domain.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.classroom.data.remote.dto.courses.GetUsersByCourseResponse
import com.example.classroom.data.remote.dto.posts.GetPostsResponseDto
import com.example.classroom.data.remote.dto.posts.PostResponseDto


@Entity("localPost_table")
data class LocalPost(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo("idApi") val idApi: String,
    @ColumnInfo("title") val title: String,
    @ColumnInfo("content") val content: String,
    @ColumnInfo("createdAt") val createdAt: String,
    @ColumnInfo("course_id") val courseId: String,
    @ColumnInfo("mediaUrl") val mediaUrl: String? = null,
    @ColumnInfo("author_id") val authorId: String
)

fun PostResponseDto.toLocal(): LocalPost {
    return  LocalPost(
        idApi = data.id.toString(),
        id = 0,
        courseId = data.courseId.toString(),
        content = data.content,
        title = data.title,
        mediaUrl = data.file,
        authorId = data.authorId.toString(),
        createdAt = data.creation,
    )
}

fun GetPostsResponseDto.toLocal(): List<LocalPost> {
    return data.map {
        LocalPost(
            idApi = it.id.toString(),
            id = 0,
            courseId = it.courseId.toString(),
            content = it.content,
            mediaUrl = it.file,
            title = it.title,
            authorId = it.authorId.toString(),
            createdAt = it.createdAt,
        )
    }
}