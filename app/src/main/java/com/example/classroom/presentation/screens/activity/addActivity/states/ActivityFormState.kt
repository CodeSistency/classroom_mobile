package com.example.classroom.presentation.screens.activity.addActivity.states

import com.example.classroom.domain.model.entity.Status

data class ActivityFormState(
    val idCourse: String = "",
    val idCourseError: String? = null,
    val owner: String = "",
    val ownerError: String? = null,
    val ownerName: String = "",
    val ownerNameError: String? = null,
    val title: String = "",
    val titleError: String? = null,
    val description: String = "",
    val descriptionError: String? = null,
    val grade: String = "0",
    val gradeError: String? = null,
    val startDate: String = "",
    val startDateError: String? = null,
    val endDate: String = "",
    val endDateError: String? = null,
    val status: Status = Status.OPEN,
    val statusError: String? = null
)