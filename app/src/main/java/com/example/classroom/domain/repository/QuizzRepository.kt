package com.example.classroom.domain.repository

import com.example.classroom.common.ResponseGenericAPi
import com.example.classroom.data.local.db.QuizWithQuestions
import com.example.classroom.data.remote.dto.quizz.AnswerQuizzDto
import com.example.classroom.data.remote.dto.quizz.CreateQuizzDto
import com.example.classroom.data.remote.dto.quizz.QuestionDto
import com.example.classroom.data.remote.dto.quizz.QuizzResponseDto
import com.example.classroom.domain.model.entity.AnswerEntity
import kotlinx.coroutines.flow.Flow

interface QuizzRepository {
    suspend fun createQuiz(activityId: Int, title: String, questions: List<QuestionDto>)
    suspend fun loadQuiz(quizId: Int): QuizWithQuestions
    suspend fun submitAnswers(answers: List<AnswerEntity>)

    suspend fun createQuizzRemote(body: CreateQuizzDto): ResponseGenericAPi<QuizzResponseDto>

    suspend fun answerQuizzRemote(body: AnswerQuizzDto): ResponseGenericAPi<QuizzResponseDto>
}