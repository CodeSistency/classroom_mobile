package com.example.classroom.presentation.screens.Quizz.addQuizz

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.classroom.R
import com.example.classroom.common.CustomButton.CustomButton
import com.example.classroom.common.CustomButton.NavigationButtonStyle
import com.example.classroom.common.CustomInput.CustomTextField
import com.example.classroom.data.remote.dto.quizz.QuestionDto

import com.example.classroom.presentation.screens.Quizz.QuizzViewModel
import com.example.classroom.presentation.theme.Azul
import com.example.classroom.presentation.theme.AzulGradient


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun CreateQuizzScreen(viewModel: QuizzViewModel, activityId: Int, navController: NavController) {
    var title by remember { mutableStateOf("") }
    var questions by remember { mutableStateOf(mutableListOf<QuestionDto>()) }

    Box(modifier = Modifier.fillMaxSize()){
        Scaffold(
            topBar = {
                Row(
                    modifier= Modifier
                        .background(Azul)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null,
                            tint = Color.White)
                    }
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(text = "Crear quizz", color = Color.White, fontSize = 16.sp)
                }
            }
        ){
            Column {
                Spacer(modifier = Modifier.padding(top = 10.dp))
                Box(modifier = Modifier.fillMaxWidth()) {
                    Image(
                        modifier = Modifier
                            .size(width = 70.dp, height = 70.dp)
                            .align(Alignment.Center)
                            .padding(vertical = 10.dp),
                        painter = painterResource(id = R.drawable.ic_logo),
                        contentDescription = "logo"
                    )

                }
                CustomTextField(value = title, onValueChange = { title = it }, label = "Quizz Titulo"){}

                questions.forEachIndexed { index, question ->

                    CustomTextField(
                        value = question.text,
                        onValueChange = { newText ->
                            questions[index] = question.copy(text = newText)
                        },
                        label = "Pregunta ${index + 1}"
                    ){}
                    question.options.forEachIndexed { optIndex, option ->
                        CustomTextField(
                            value = option,
                            onValueChange = { newOption ->
                                // Modify the option in a mutable way
                                val updatedOptions = question.options.toMutableList()
                                updatedOptions[optIndex] = newOption
                                questions[index] = question.copy(options = updatedOptions)
                            },
                            label = "Opción ${optIndex + 1}"
                        ){}
                    }
                }

                Button(onClick = {
                    viewModel.createQuiz(activityId, title, questions)
                }) {
                    Text("Crear quizz")
                }
            }

        }
    }

}
