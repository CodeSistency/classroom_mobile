package com.example.classroom.presentation.screens.course.posts.addPost.composable

import com.example.classroom.domain.model.entity.LocalPost

data class AddPostState(
    val info: LocalPost? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)