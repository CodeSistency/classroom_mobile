package com.example.classroom.common

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import com.example.classroom.data.local.db.AppDao
import com.example.classroom.domain.model.entity.Area
import com.example.classroom.domain.model.entity.Gender
import com.example.classroom.domain.model.entity.LocalActivities
import com.example.classroom.domain.model.entity.LocalActivitySubmission
import com.example.classroom.domain.model.entity.LocalCourses
import com.example.classroom.domain.model.entity.LocalPost
import com.example.classroom.domain.model.entity.LocalStudents
import com.example.classroom.domain.model.entity.LocalUser
import com.example.classroom.domain.model.entity.QuestionsEntity
import com.example.classroom.domain.model.entity.QuizzEntity
import com.example.classroom.domain.model.entity.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import proyecto.person.appconsultapopular.data.local.db.AppDatabase
import java.util.UUID
import kotlin.random.Random

class Seeders(
    private val lifecycle: Lifecycle
) {



    fun seedDatabase(dao: AppDao)  {
        lifecycle.coroutineScope.launch {
            seedUsers(dao)
            seedCourses(dao)
            seedStudents(dao)
            seedActivities(dao)
            seedPosts(dao)
            seedQuizzes(dao)
            seedSubmissions(dao)
        }

    }

    private suspend fun seedUsers(dao: AppDao) {
        // Seed only the logged-in user
        val users = listOf(
            LocalUser(
                idApi = "user1",
                name = "Alice",
                lastname = "Smith",
                email = "alice@example.com",
                gender = Gender.Woman,
                birthdate = "1990-01-01",
                phone = "123-456-7890",
                isLogged = true,
                coursesId = listOf(1, 2) // References both created and joined courses
            )
        )
        users.forEach { dao.insertOrUpdateUser(it) }
    }

    private suspend fun seedCourses(dao: AppDao) {
        // Course created by the user (user acts as a professor)
        val createdCourse = LocalCourses(
            idApi = "course1",
            title = "Mathematics 101",
            description = "Basic math concepts",
            owner = "user1", // Owner is the logged-in user
            ownerName = "Alice",
            section = "A",
            subject = "Mathematics",
            token = "token123",
            area = Area.MATH
        )

        // Course joined by the user (user acts as a student)
        val joinedCourse = LocalCourses(
            idApi = "course2",
            title = "Biology Basics",
            description = "Introduction to Biology",
            owner = "professor2", // Owned by another professor
            ownerName = "Dr. Bob",
            section = "B",
            subject = "Biology",
            token = "token456",
            area = Area.BIOLOGY
        )

        listOf(createdCourse, joinedCourse).forEach { dao.insertOrUpdateCourse(it) }
    }

    private suspend fun seedStudents(dao: AppDao) {
        // Students enrolled in the course created by the logged-in user
        val students = listOf(
            LocalStudents(
                idApi = "stu1",
                courseId = "course1",
                name = "Charlie",
                lastname = "Brown",
                email = "charlie@example.com",
                gender = Gender.Man,
                birthdate = "2001-04-04",
                phone = "456-789-0123"
            ),
            LocalStudents(
                idApi = "stu2",
                courseId = "course1",
                name = "Daisy",
                lastname = "Duke",
                email = "daisy@example.com",
                gender = Gender.Woman,
                birthdate = "2002-05-05",
                phone = "567-890-1234"
            )
        )
        students.forEach { dao.insertOrUpdateStudent(it) }
    }

    private suspend fun seedActivities(dao: AppDao) {
        // Activities in the course owned by the user
        val activities = listOf(
            LocalActivities(
                idApi = "activity1",
                idCourse = "course1",
                title = "Math Homework 1",
                description = "Complete exercises 1-10",
                grade = 100,
                startDate = "2023-01-01",
                endDate = "2023-01-10",
                status = Status.OPEN
            ),
            LocalActivities(
                idApi = "activity2",
                idCourse = "course2",
                title = "Biology Lab Report",
                description = "Document your findings from the lab",
                grade = 100,
                startDate = "2023-02-01",
                endDate = "2023-02-10",
                status = Status.OPEN
            )
        )
        activities.forEach { dao.insertOrUpdateActivity(it) }
    }

    private suspend fun seedPosts(dao: AppDao) {
        // Posts for each course
        val posts = listOf(
            LocalPost(
                title = "Welcome to Mathematics 101",
                content = "This course covers basic math concepts and problem-solving.",
                createdAt = "2023-01-01",
                courseId = 1, // Reference to created course
                activityId = null,
                authorId = 1 // Reference to the logged-in user
            ),
            LocalPost(
                title = "Biology Introduction",
                content = "This course covers basic concepts in biology.",
                createdAt = "2023-02-01",
                courseId = 2, // Reference to joined course
                activityId = null,
                authorId = 2 // Reference to another professor
            )
        )
        posts.forEach { dao.insertOrUpdatePost(it) }
    }

    private suspend fun seedQuizzes(dao: AppDao) {
        val quizzes = listOf(
            QuizzEntity(activityId = 1, courseId = 1),
            QuizzEntity(activityId = 2, courseId = 2)
        )
        quizzes.forEach { dao.insertOrUpdateQuiz(it) }

        val questions = listOf(
            QuestionsEntity(
                quizzId = 1,
                courseId = 1,
                text = "What is 2 + 2?",
                answer = 4
            ),
            QuestionsEntity(
                quizzId = 2,
                courseId = 2,
                text = "What is the function of mitochondria?",
                answer = 1
            )
        )
        questions.forEach { dao.insertOrUpdateQuestion(it) }
    }

    private suspend fun seedSubmissions(dao: AppDao) {
        // Submission in a joined course by the user
        val submissions = listOf(
            LocalActivitySubmission(
                idApi = "sub1",
                activityId = "activity2", // Activity in joined course
                courseId = "course2",
                studentId = "user1", // Logged-in user
                submissionDate = "2023-02-03",
                comment = "Completed the lab report with findings",
                documentUrl = "https://example.com/doc1",
                grade = 90f
            ),
            LocalActivitySubmission(
                idApi = "sub2",
                activityId = "activity1", // Activity in owned course
                courseId = "course1",
                studentId = "stu1", // Student enrolled in created course
                submissionDate = "2023-01-05",
                comment = "Finished the math homework",
                documentUrl = "https://example.com/doc2",
                grade = 85f
            )
        )
        submissions.forEach { dao.insertOrUpdateSubmission(it) }
    }


}