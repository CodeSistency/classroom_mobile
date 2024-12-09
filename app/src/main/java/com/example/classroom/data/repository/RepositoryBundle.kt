package com.example.classroom.data.repository

import com.example.classroom.domain.repository.ActivitiesRepository
import com.example.classroom.domain.repository.ChatRepository
import com.example.classroom.domain.repository.CloudRepository
import com.example.classroom.domain.repository.CoursesRepository
import com.example.classroom.domain.repository.LoginRepository
import com.example.classroom.domain.repository.PostsRepository
import com.example.classroom.domain.repository.QuizzRepository
import com.example.classroom.domain.repository.StudentRepository
import com.example.classroom.domain.repository.SubmissionsRepository

data class RepositoryBundle(
    val loginRepository: LoginRepository,
    val coursesRepository: CoursesRepository,
    val activitiesRepository: ActivitiesRepository,
    val studentsRepository: StudentRepository,
    val quizzRepository: QuizzRepository,
    val submissionsRepository: SubmissionsRepository,
    val postsRepositoryImpl: PostsRepository,
    val cloudRepository: CloudRepository,
    val chatRepository: ChatRepository
)
