package com.example.classroom.presentation.screens.course.AddCourse.states

import com.example.classroom.domain.model.entity.Area


data class CourseFormState(
    val title: String = "",
    val titleError: String? = null,
    val description: String = "",
    val descriptionError: String? = null,
    val owner: String = "",
    val ownerError: String? = null,
    val ownerName: String = "",
    val ownerNameError: String? = null,
    val section: String = "",
    val sectionError: String? = null,
    val subject: String = "",
    val subjectError: String? = null,
    val area: Area = Area.NO_SELECTED,
    val areaError: String? = null
)
