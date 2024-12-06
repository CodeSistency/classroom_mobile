package com.example.classroom.domain.repository

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.classroom.common.ResponseGenericAPi
import com.example.classroom.data.local.db.daos.QuizWithQuestions
import com.example.classroom.data.remote.dto.quizz.AnswerQuizzDto
import com.example.classroom.data.remote.dto.quizz.AnswerQuizzResponseDto
import com.example.classroom.data.remote.dto.quizz.CreateQuizzDto
import com.example.classroom.data.remote.dto.quizz.CreateQuizzResponseDto
import com.example.classroom.data.remote.dto.quizz.QuestionDto
import com.example.classroom.domain.model.entity.AnswerEntity
import com.example.classroom.domain.model.entity.OptionEntity
import com.example.classroom.domain.model.entity.QuestionEntity
import com.example.classroom.domain.model.entity.QuizEntity

interface QuizzRepository {
    suspend fun createQuiz(activityId: Int, title: String, questions: List<QuestionDto>)
    suspend fun loadQuiz(quizId: Int): QuizWithQuestions
    suspend fun submitAnswers(answers: List<AnswerEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuiz(quiz: QuizEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(question: QuestionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOption(option: OptionEntity)

    suspend fun createQuizzRemote(body: CreateQuizzDto): ResponseGenericAPi<CreateQuizzResponseDto>

    suspend fun answerQuizzRemote(body: AnswerQuizzDto, idQuizz: String): ResponseGenericAPi<AnswerQuizzResponseDto>

    suspend fun insertAnswer(answer: AnswerEntity)

}