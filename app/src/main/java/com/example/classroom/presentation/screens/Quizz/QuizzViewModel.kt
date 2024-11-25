package com.example.classroom.presentation.screens.Quizz

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classroom.data.local.db.QuizWithQuestions
import com.example.classroom.data.remote.dto.quizz.QuestionDto
import com.example.classroom.data.repository.RepositoryBundle
import com.example.classroom.domain.model.entity.AnswerEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


class QuizzViewModel(private val repositoryBundle: RepositoryBundle,
) : ViewModel() {

    private val quizzRepository = repositoryBundle.quizzRepository

    val quizState = mutableStateOf<QuizWithQuestions?>(null)

    fun createQuiz(activityId: Int, title: String, questions: List<QuestionDto>) {
        viewModelScope.launch {
            quizzRepository.createQuiz(activityId, title, questions)
        }
    }

    fun loadQuiz(quizId: Int) {
        viewModelScope.launch {
            quizState.value = quizzRepository.loadQuiz(quizId)
        }
    }

    fun submitAnswers(answers: List<AnswerEntity>) {
        viewModelScope.launch {
            quizzRepository.submitAnswers(answers)
        }
    }
}
