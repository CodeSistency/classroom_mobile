package com.example.classroom.presentation.screens.course.posts.addPost

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class AddPostViewModel : ViewModel() {
    var title = mutableStateOf("")
    var content = mutableStateOf("")
    var courseId = mutableStateOf(0)
    var authorId = mutableStateOf(0)

    // Validation States
    var titleError = mutableStateOf<String?>(null)
    var contentError = mutableStateOf<String?>(null)
    var courseIdError = mutableStateOf<String?>(null)
    var authorIdError = mutableStateOf<String?>(null)

    // Form Validity
    val isFormValid: Boolean
        get() = titleError.value == null &&
                contentError.value == null &&
                courseIdError.value == null &&
                authorIdError.value == null &&
                title.value.isNotBlank() &&
                content.value.isNotBlank() &&
                courseId.value > 0 &&
                authorId.value > 0

    // Validation Methods
    fun validateTitle() {
        titleError.value = if (title.value.isBlank()) "Title is required" else null
    }

    fun validateContent() {
        contentError.value = if (content.value.isBlank()) "Content is required" else null
    }

    fun validateCourseId() {
        courseIdError.value = if (courseId.value <= 0) "Course ID must be greater than zero" else null
    }

    fun validateAuthorId() {
        authorIdError.value = if (authorId.value <= 0) "Author ID must be greater than zero" else null
    }

    fun resetForm() {
        title.value = ""
        content.value = ""
        courseId.value = 0
        authorId.value = 0
    }
}