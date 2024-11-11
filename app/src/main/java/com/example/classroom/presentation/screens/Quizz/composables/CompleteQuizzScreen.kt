package com.example.classroom.presentation.screens.Quizz.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType.Companion.Text
import androidx.compose.ui.unit.dp
import com.example.classroom.presentation.screens.Quizz.QuizzViewModel

@Composable
fun CompleteQuizzScreen(viewModel: QuizzViewModel, courseId: String, quizzId: Int) {
    val quizzWithQuestionsFlow = viewModel.getQuizzWithQuestions(courseId, quizzId).collectAsState(initial = null)
    var answers = remember { mutableStateMapOf<Int, String>() }

    quizzWithQuestionsFlow.value?.let { data ->
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Quiz: ${data.quizz.id}", style = MaterialTheme.typography.h5)
            Spacer(modifier = Modifier.height(16.dp))
            data.questions.forEach { question ->
                Text(text = "Question: ${question.text}")
                OutlinedTextField(
                    value = answers[question.id] ?: "",
                    onValueChange = { answers[question.id] = it },
                    label = { Text("Your Answer") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                // Handle quiz submission logic here
            }) {
                Text("Submit Quiz")
            }
        }
    }
}