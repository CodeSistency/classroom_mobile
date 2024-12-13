package com.example.classroom.common.composables.lists

data class PaginationState<T>(
    val items: List<T> = emptyList(),
    val currentPage: Int = 1,
    val isEndOfList: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)