package com.example.classroom.data.repository

import com.example.classroom.common.ResponseGenericAPi
import com.example.classroom.data.local.db.AppDao
import com.example.classroom.data.local.db.QuizWithQuestions
import com.example.classroom.data.local.db.QuizzDao
import com.example.classroom.data.remote.ApiService
import com.example.classroom.data.remote.dto.quizz.AnswerQuizzDto
import com.example.classroom.data.remote.dto.quizz.CreateQuizzDto
import com.example.classroom.data.remote.dto.quizz.QuestionDto
import com.example.classroom.data.remote.dto.quizz.QuizzResponseDto
import com.example.classroom.domain.model.entity.AnswerEntity
import com.example.classroom.domain.model.entity.OptionEntity
import com.example.classroom.domain.model.entity.QuestionEntity
import com.example.classroom.domain.model.entity.QuizEntity
import com.example.classroom.domain.repository.QuizzRepository
import kotlinx.coroutines.flow.Flow

class QuizzRepositoryImpl(
    private val apiService: ApiService,
    private val quizDao: QuizzDao
): QuizzRepository {
    override suspend fun createQuiz(activityId: Int, title: String, questions: List<QuestionDto>) {
        val quizId = quizDao.insertQuiz(QuizEntity(activityId = activityId, title = title)).toInt()

        val questionEntities = questions.map { question ->
            QuestionEntity(quizId = quizId, text = question.text, correctAnswer = question.answer)
        }
        quizDao.insertQuestions(questionEntities)

        val optionEntities = questions.flatMapIndexed { index, question ->
            question.options.map { OptionEntity(questionId = questionEntities[index].id, text = it) }
        }
        quizDao.insertOptions(optionEntities)
    }

    override suspend fun loadQuiz(quizId: Int): QuizWithQuestions {
        return quizDao.getQuizWithQuestions(quizId)
    }

    override suspend fun submitAnswers(answers: List<AnswerEntity>) {
        quizDao.insertAnswers(answers)
    }

    override suspend fun createQuizzRemote(body: CreateQuizzDto): ResponseGenericAPi<QuizzResponseDto> {
        return apiService.createQuizzRemote(body)
    }

    override suspend fun answerQuizzRemote(body: AnswerQuizzDto): ResponseGenericAPi<QuizzResponseDto> {
        return apiService.answerQuizzRemote(body)
    }
}