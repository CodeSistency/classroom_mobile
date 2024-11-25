package com.example.classroom.presentation.screens.Quizz.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType.Companion.Text
import androidx.compose.ui.unit.dp
import com.example.classroom.presentation.screens.Quizz.QuizzViewModel

@Composable
fun AnswerQuizScreen(viewModel: QuizzViewModel, quizId: Int) {
    val quizState by viewModel.quizState

    LaunchedEffect(Unit) {
        viewModel.loadQuiz(quizId)
    }

    quizState?.let { quiz ->
        Column {
            quiz.questions.forEach { question ->
                Text(question.question.text)

                question.options.forEach { option ->
                    Row {
                        RadioButton(
                            selected = false,
                            onClick = { /* Save selected answer */ }
                        )
                        Text(option.text)
                    }
                }
            }

            Button(onClick = { /* Submit answers logic */ }) {
                Text("Submit Quiz")
            }
        }
    }
}
