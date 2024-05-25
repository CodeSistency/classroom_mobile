package com.example.classroom.data.repository

import com.example.classroom.domain.repository.ActivitiesRepository
import com.example.classroom.domain.repository.CoursesRepository
import com.example.classroom.domain.repository.LoginRepository

data class RepositoryBundle(
    val loginRepository: LoginRepository,
    val coursesRepository: CoursesRepository,
    val activitiesRepository: ActivitiesRepository
)
