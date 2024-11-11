package com.example.classroom.data.repository

import com.example.classroom.data.local.db.AppDao
import com.example.classroom.data.remote.ApiService
import com.example.classroom.domain.model.entity.QuestionsEntity
import com.example.classroom.domain.model.entity.QuizzEntity
import com.example.classroom.domain.model.entity.QuizzWithQuestions
import com.example.classroom.domain.repository.QuizzRepository
import kotlinx.coroutines.flow.Flow

class QuizzRepositoryImpl(
    private val apiService: ApiService,
    private val dao: AppDao
): QuizzRepository {
    override fun getQuizzWithQuestions(courseId: String, quizzId: Int): Flow<QuizzWithQuestions> {
        return dao.getQuizzWithQuestions(courseId, quizzId)
    }

    override suspend fun insertQuizz(quizz: QuizzEntity) {
        dao.insertQuizz(quizz)
    }

    override suspend fun insertQuestion(question: QuestionsEntity) {
        dao.insertQuestion(question)
    }

    override fun getQuestionsForCourse(courseId: String): Flow<List<QuestionsEntity>> {
        return dao.getQuestionsForCourse(courseId)
    }
}