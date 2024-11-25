package com.example.classroom.presentation.screens.Quizz.addQuizz

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.classroom.common.CustomButton.CustomButton
import com.example.classroom.common.CustomButton.NavigationButtonStyle
import com.example.classroom.common.CustomInput.CustomTextField
import com.example.classroom.data.remote.dto.quizz.QuestionDto

import com.example.classroom.presentation.screens.Quizz.QuizzViewModel
import com.example.classroom.presentation.theme.Azul
import com.example.classroom.presentation.theme.AzulGradient


@Composable
fun CreateQuizzScreen(viewModel: QuizzViewModel, activityId: Int) {
    var title by remember { mutableStateOf("") }
    var questions by remember { mutableStateOf(mutableListOf<QuestionDto>()) }

    Column {
        TextField(value = title, onValueChange = { title = it }, label = { Text("Quiz Title") })

        questions.forEachIndexed { index, question ->
            TextField(
                value = question.text,
                onValueChange = { newText ->
                    questions[index] = question.copy(text = newText)
                },
                label = { Text("Question ${index + 1}") }
            )
            question.options.forEachIndexed { optIndex, option ->
                TextField(
                    value = option,
                    onValueChange = { newOption ->
                        // Modify the option in a mutable way
                        val updatedOptions = question.options.toMutableList()
                        updatedOptions[optIndex] = newOption
                        questions[index] = question.copy(options = updatedOptions)
                    },
                    label = { Text("Option ${optIndex + 1}") }
                )
            }
        }

        Button(onClick = {
            viewModel.createQuiz(activityId, title, questions)
        }) {
            Text("Save Quiz")
        }
    }
}
