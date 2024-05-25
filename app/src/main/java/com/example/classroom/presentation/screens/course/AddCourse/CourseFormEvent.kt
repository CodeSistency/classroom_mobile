package com.example.classroom.presentation.screens.course.AddCourse

import com.example.classroom.data.remote.dto.courses.CourseRequestDto
import com.example.classroom.domain.model.entity.Area

sealed class CourseFormEvent {
    data class TitleChanged(val title : String) : CourseFormEvent()
    data class DescriptionChanged(val description : String) : CourseFormEvent()
    data class SectionChanged(val section : String) : CourseFormEvent()
    data class SubjectChanged(val subject : String) : CourseFormEvent()
    data class AreaChanged(val area : Area) : CourseFormEvent()
    data class Submit(val id : String?, val body: CourseRequestDto) : CourseFormEvent()
}