package com.example.classroom.presentation.screens.activity.addActivity.states

import com.example.classroom.data.remote.dto.activities.ActivityRequestDto
import com.example.classroom.domain.model.entity.Status


sealed class ActivityFormEvent {
    data class TitleChanged(val title : String) : ActivityFormEvent()
    data class DescriptionChanged(val description : String) : ActivityFormEvent()
    data class GradeChanged(val grade : Int) : ActivityFormEvent()
    data class StartDateChanged(val date : String) : ActivityFormEvent()
    data class EndDateChanged(val date : String) : ActivityFormEvent()
    data class StatusChanged(val status : Status) : ActivityFormEvent()
    data class Submit(val id : String?, val body: ActivityRequestDto) : ActivityFormEvent()
}