package com.example.classroom.data.repository

import com.example.classroom.data.local.db.AppDao
import com.example.classroom.data.remote.ApiService
import com.example.classroom.domain.repository.StudentRepository

class StudentsRepositoryImpl(
    private val apiService: ApiService,
    private val dao: AppDao
): StudentRepository {
}