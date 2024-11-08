package com.example.classroom.domain.repository

import com.example.classroom.domain.model.entity.QuestionsEntity
import com.example.classroom.domain.model.entity.QuizzEntity
import com.example.classroom.domain.model.entity.QuizzWithQuestions
import kotlinx.coroutines.flow.Flow

interface QuizzRepository {
    fun getQuizzWithQuestions(courseId: Int, quizzId: Int): Flow<QuizzWithQuestions>
    suspend fun insertQuizz(quizz: QuizzEntity)
    suspend fun insertQuestion(question: QuestionsEntity)
    fun getQuestionsForCourse(courseId: Int): Flow<List<QuestionsEntity>>
}