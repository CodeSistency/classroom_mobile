package com.example.classroom.presentation.screens.Quizz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classroom.data.repository.RepositoryBundle
import com.example.classroom.domain.model.entity.QuestionsEntity
import com.example.classroom.domain.model.entity.QuizzEntity
import com.example.classroom.domain.model.entity.QuizzWithQuestions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


class QuizzViewModel(private val repositoryBundle: RepositoryBundle,
) : ViewModel() {

    fun getQuizzWithQuestions(courseId: Int, quizzId: Int): Flow<QuizzWithQuestions> {
        return repositoryBundle.quizzRepository.getQuizzWithQuestions(courseId, quizzId)
    }

    fun insertQuizz(quizz: QuizzEntity) {
        viewModelScope.launch {
            repositoryBundle.quizzRepository.insertQuizz(quizz)
        }
    }

    fun insertQuestion(question: QuestionsEntity) {
        viewModelScope.launch {
            repositoryBundle.quizzRepository.insertQuestion(question)
        }
    }

    fun getQuestionsForCourse(courseId: Int): Flow<List<QuestionsEntity>> {
        return repositoryBundle.quizzRepository.getQuestionsForCourse(courseId)
    }
}
