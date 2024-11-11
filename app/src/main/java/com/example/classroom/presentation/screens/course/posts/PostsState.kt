package com.example.classroom.presentation.screens.course.posts

import com.example.classroom.domain.model.entity.LocalPost
data class PostsState(
    val info: List<LocalPost>? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)