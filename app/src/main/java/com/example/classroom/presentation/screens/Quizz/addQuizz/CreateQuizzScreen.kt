package com.example.classroom.presentation.screens.Quizz.addQuizz

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
import com.example.classroom.domain.model.entity.QuestionsEntity
import com.example.classroom.domain.model.entity.QuizzEntity
import com.example.classroom.presentation.screens.Quizz.QuizzViewModel
import com.example.classroom.presentation.theme.Azul
import com.example.classroom.presentation.theme.AzulGradient

@Composable
fun CreateQuizzScreen(viewModel: QuizzViewModel, courseId: String, navController: NavController) {
    var title by remember { mutableStateOf("") }
    var questions = remember { mutableStateListOf<String>() }
    var newQuestion by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Create a Quiz", style = MaterialTheme.typography.h5)
        Spacer(modifier = Modifier.height(16.dp))
        CustomTextField(
            value = title,
            onValueChange = { title = it },
            label = "Titulo del quizz",
            modifier = Modifier.fillMaxWidth(),
        ){}
        Spacer(modifier = Modifier.height(16.dp))
        CustomTextField(
            value = newQuestion,
            onValueChange = { newQuestion = it },
            label =  "New Question",
            modifier = Modifier.fillMaxWidth()
        ){}
        Spacer(modifier = Modifier.height(8.dp))
        CustomButton(onClick = {
            if (newQuestion.isNotBlank()) {
                questions.add(newQuestion)
                newQuestion = ""
            }
        },
            text = "Agrega una pregunta",
            style = NavigationButtonStyle.SolidGradient,
            color1 = Azul,
            color2 = AzulGradient,

        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Questions:")
        questions.forEach { question ->
            Text(text = question)
        }
        Spacer(modifier = Modifier.height(16.dp))
        CustomButton(onClick = {
            val quizz = QuizzEntity(activityId = "0", courseId = courseId)
            viewModel.insertQuizz(quizz)
            questions.forEach { questionText ->
                val question = QuestionsEntity(quizzId = quizz.id, courseId = courseId, text = questionText, answer = 0)
                viewModel.insertQuestion(question)
            }
        },
            text = "Agrega una pregunta",
            style = NavigationButtonStyle.SolidGradient,
            color1 = Azul,
            color2 = AzulGradient,
            )
    }
}