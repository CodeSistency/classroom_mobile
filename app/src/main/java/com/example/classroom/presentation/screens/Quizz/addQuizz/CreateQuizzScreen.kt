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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.classroom.R
import com.example.classroom.common.CustomButton.CustomButton
import com.example.classroom.common.CustomButton.NavigationButtonStyle
import com.example.classroom.common.CustomDatePicker.CustomDatePicker
import com.example.classroom.common.CustomInput.CustomTextField
import com.example.classroom.common.composables.customDialogs.SetupCustomDialog
import com.example.classroom.common.composables.customDialogs.SetupCustomDialogState
import com.example.classroom.common.customSelect.CustomSelect
import com.example.classroom.data.remote.dto.quizz.QuestionDto
import com.example.classroom.domain.model.entity.Status

import com.example.classroom.presentation.screens.Quizz.QuizzViewModel
import com.example.classroom.presentation.theme.Azul
import com.example.classroom.presentation.theme.AzulGradient
import kotlinx.coroutines.delay


//@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
//@Composable
//fun CreateQuizzScreen(viewModel: QuizzViewModel, courseId: String, navController: NavController, focusManager: FocusManager) {
//    var questions by remember { mutableStateOf(mutableListOf<QuestionDto>()) }
//    val state = viewModel.stateCreateQuizz.collectAsState()
//    var dialogState: SetupCustomDialogState by remember {
//        mutableStateOf(SetupCustomDialogState.Default())
//    }
//
//    Scaffold(
//        topBar = {
//            Row(
//                modifier = Modifier
//                    .background(Azul)
//                    .fillMaxWidth(),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                IconButton(onClick = { navController.popBackStack() }) {
//                    Icon(
//                        imageVector = Icons.Default.ArrowBack,
//                        contentDescription = null,
//                        tint = Color.White
//                    )
//                }
//                Spacer(modifier = Modifier.width(3.dp))
//                Text(text = "Crear Quizz", color = Color.White, fontSize = 16.sp)
//            }
//        }
//    ) {
//        LazyColumn(modifier = Modifier.padding(16.dp)) {
//
//            item {
//                // Title
//                CustomTextField(
//                    value = viewModel.title.value,
//                    onValueChange = { viewModel.title.value = it },
//                    label = "Título",
//                    errorMessage = viewModel.titleError.value ?: "",
//                    onNextClick = { focusManager.moveFocus(FocusDirection.Down) }
//                )
//
//                Spacer(modifier = Modifier.height(8.dp))
//
//                // Questions
//                questions.forEachIndexed { index, question ->
//                    Text("Pregunta ${index + 1}", style = MaterialTheme.typography.h6)
//
//                    // Question text
//                    CustomTextField(
//                        value = question.text,
//                        onValueChange = { newText ->
//                            questions[index] = question.copy(text = newText)
//                        },
//                        label = "Texto de la pregunta",
//                        errorMessage = if (question.text.isBlank()) "La pregunta no puede estar vacía" else "",
//                        onNextClick = { focusManager.moveFocus(FocusDirection.Down) }
//
//                    )
//
//                    // Options
//                    question.options.forEachIndexed { optIndex, option ->
//                        Row(verticalAlignment = Alignment.CenterVertically) {
//                            CustomTextField(
//                                value = option,
//                                onValueChange = { newOption ->
//                                    questions[index].options[optIndex] = newOption
//                                },
//                                label = "Opción ${optIndex + 1}",
//                                errorMessage = if (question.text.isBlank()) "La pregunta no puede estar vacía" else "",
//                                onNextClick = { focusManager.moveFocus(FocusDirection.Down) }
//                            )
//                            Spacer(modifier = Modifier.width(8.dp))
//                            Checkbox(
//                                checked = question.answer == optIndex,
//                                onCheckedChange = {
//                                    questions[index] = question.copy(answer = optIndex)
//                                }
//                            )
//                            Text("Correcta")
//                        }
//                    }
//
//                    // Add Option Button
//                    Button(onClick = {
//                        if (question.options.size < 4) {
//                            questions[index].options.add("")
//                        }
//                    }) {
//                        Text("Añadir Opción")
//                    }
//
//                    Spacer(modifier = Modifier.height(16.dp))
//                }
//
//                // Add Question Button
//                Button(onClick = {
//                    questions.add(QuestionDto(text = "", options = mutableListOf("", ""), answer = -1))
//                }) {
//                    Text("Añadir Pregunta")
//                }
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                // Create Quiz Button
//                CustomButton(
//                    text = "Crear Quizz",
//                    color1 = Azul,
//                    color2 = AzulGradient,
//                    style = NavigationButtonStyle.SolidGradient,
//                    onClick = {
//                        viewModel.createQuizRemote(idCourse = courseId, questions)
//                    })
//            }
//
//        }
//    }
//
//    LaunchedEffect(key1 = state.value, block = {
//        when{
//            state.value.isLoading -> {
//                dialogState = SetupCustomDialogState.Loading()
//            }
//            state.value.error != null -> {
//                dialogState = SetupCustomDialogState.Error(state.value.error?.uiMessage)
//            }
//
//            else -> {
//                if (state.value.info != null){
//                    dialogState = SetupCustomDialogState.Success(message = "Se ha creado la actividad exitosamente")
//                    delay(1000)
//                    navController.popBackStack()
////                    viewModel.resetForm()
//                }
//            }
//        }
//    })
//
//    SetupCustomDialog(setupCustomDialogState = dialogState, showDialog = dialogState != SetupCustomDialogState.Default()) {
//        dialogState = SetupCustomDialogState.Default()
//    }
//}


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun CreateQuizzScreen(viewModel: QuizzViewModel, courseId: String, navController: NavController, focusManager: FocusManager) {
    val questions by viewModel.questions

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .background(Azul)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(3.dp))
                Text(text = "Crear Quizz", color = Color.White, fontSize = 16.sp)
            }
        }
    ) {
        LazyColumn(modifier = Modifier.padding(horizontal = 6.dp)) {
            item {
                // Title
                CustomTextField(
                    value = viewModel.title.value,
                    onValueChange = { viewModel.title.value = it },
                    label = "Título",
                    errorMessage = viewModel.titleError.value ?: "",
                    onNextClick = { focusManager.moveFocus(FocusDirection.Down) }
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Description
                CustomTextField(
                    value = viewModel.description.value,
                    onValueChange = { viewModel.description.value = it },
                    label = "Descripción",
                    errorMessage = viewModel.descriptionError.value ?: "",
                    onNextClick = { focusManager.moveFocus(FocusDirection.Down) }
                )

                Spacer(modifier = Modifier.height(6.dp))

                questions.forEachIndexed { index, question ->
                    Text("Pregunta ${index + 1}", style = MaterialTheme.typography.h6)

                    // Question text
                    CustomTextField(
                        value = question.text,
                        onValueChange = { viewModel.updateQuestionText(index, it) },
                        label = "Texto de la pregunta",
                        errorMessage = if (question.text.isBlank()) "La pregunta no puede estar vacía" else "",
                        onNextClick = { focusManager.moveFocus(FocusDirection.Down) }
                    )

                    // Options
                    question.options.forEachIndexed { optIndex, option ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.fillMaxWidth(0.8f)) {
                                CustomTextField(
                                    value = option,
                                    onValueChange = { viewModel.updateOptionText(index, optIndex, it) },
                                    label = "Opción ${optIndex + 1}",
                                    errorMessage = if (option.isBlank()) "La opción no puede estar vacía" else "",
                                    onNextClick = { focusManager.moveFocus(FocusDirection.Down) }
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Checkbox(
                                checked = question.answer == optIndex,
                                onCheckedChange = {
                                    viewModel.setCorrectAnswer(index, optIndex)
                                }
                            )
                            Text("Correcta")
                        }
                    }

                    // Add Option Button
                    Button(onClick = { viewModel.addOption(index) }) {
                        Text("Añadir Opción")
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Add Question Button
                Button(onClick = { viewModel.addQuestion() }) {
                    Text("Añadir Pregunta")
                }

                Spacer(modifier = Modifier.height(16.dp))

                val questions = viewModel.questions.value.toList()
                // Submit Button
                CustomButton(
                    text = "Crear Quizz",
                    color1 = Azul,
                    color2 = AzulGradient,
                    style = NavigationButtonStyle.SolidGradient,
                    onClick = {
                        viewModel.createQuizRemote(idCourse = courseId, questions = questions)
                    }
                )
            }
        }
    }
}
