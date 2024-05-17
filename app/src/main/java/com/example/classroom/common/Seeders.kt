package com.example.classroom.common

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import com.example.classroom.domain.model.entity.Area
import com.example.classroom.domain.model.entity.Gender
import com.example.classroom.domain.model.entity.LocalCourses
import com.example.classroom.domain.model.entity.LocalUser
import kotlinx.coroutines.launch
import proyecto.person.appconsultapopular.data.local.db.AppDatabase
import kotlin.random.Random

class Seeders(
    private val lifecycle: Lifecycle
) {

    fun addCourses(db: AppDatabase) {
        val courses = mutableListOf<LocalCourses>()

        val courseTitles = listOf("Physics 101", "Calculus I", "English Literature", "Introduction to Biology", "Computer Science Basics")
        val courseDescriptions = listOf(
            "An introduction to basic physics principles.",
            "Fundamentals of calculus.",
            "Study of classic and modern literature.",
            "Overview of biological concepts.",
            "Basic concepts in computer science."
        )
        val owners = listOf("admin", "user1", "user2", "user3", "user4")
        val ownerNames = listOf("Admin", "User One", "User Two", "User Three", "User Four")
        val sections = listOf("Section A", "Section B", "Section C", "Section D", "Section E")
        val subjects = listOf("Science", "Math", "Literature", "Biology", "CS")

        val users = listOf(
            LocalUser(idApi = "U1", name = "Alice", lastname = "Smith", email = "alice@example.com", gender = Gender.Woman, birthdate = "1990-01-01", phone = "123-456-7890"),
            LocalUser(idApi = "U2", name = "Bob", lastname = "Johnson", email = "bob@example.com", gender = Gender.Man, birthdate = "1985-05-23", phone = "123-456-7891"),
            LocalUser(idApi = "U3", name = "Charlie", lastname = "Lee", email = "charlie@example.com", gender = Gender.Other, birthdate = "1992-08-15", phone = "123-456-7892"),
            LocalUser(idApi = "U4", name = "Diana", lastname = "Brown", email = "diana@example.com", gender = Gender.Woman, birthdate = "1988-11-30", phone = "123-456-7893"),
            LocalUser(idApi = "U5", name = "Evan", lastname = "Davis", email = "evan@example.com", gender = Gender.Man, birthdate = "1995-02-18", phone = "123-456-7894")
        )

        for (i in 0 until 5) {
            val course = LocalCourses(
                idApi = "C${100 + i}",
                title = courseTitles[i],
                description = courseDescriptions[i],
                owner = owners[i],
                ownerName = ownerNames[i],
                section = sections[i],
                subject = subjects[i],
                area = Area.values().random(),
                users = users.shuffled().take(Random.nextInt(1, 4)) // Randomly assign 1 to 3 users to each course
            )
            lifecycle.coroutineScope.launch {
                db.appDao.insertLocalCourse(course)
            }

        }


    }}