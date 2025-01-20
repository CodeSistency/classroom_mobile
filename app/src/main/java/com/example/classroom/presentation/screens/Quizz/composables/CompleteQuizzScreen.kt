package com.example.classroom.presentation.screens.Quizz.composables

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType.Companion.Text
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.classroom.common.composables.CustomButton.CustomButton
import com.example.classroom.common.composables.CustomButton.NavigationButtonStyle
import com.example.classroom.common.composables.customDialogs.SetupCustomDialog
import com.example.classroom.common.composables.customDialogs.SetupCustomDialogState
import com.example.classroom.presentation.screens.Quizz.QuizzViewModel
import com.example.classroom.presentation.theme.Azul
import com.example.classroom.presentation.theme.AzulGradient
import kotlinx.coroutines.delay

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AnswerQuizScreen(viewModel: QuizzViewModel, quizId: String, navController: NavController) {
    val quizState by viewModel.quizState.collectAsState()
    val selectedOptions by viewModel.selectedOptions.collectAsState()
    val state = viewModel.stateAnswerQuizz.collectAsState()
    var dialogState: SetupCustomDialogState by remember { mutableStateOf(SetupCustomDialogState.Default()) }

    LaunchedEffect(Unit) {
        viewModel.loadQuiz(quizId.toInt())
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Responder Quizz", color = Color.White) },
                    backgroundColor = Azul,
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                    }
                )
            }
        ) {
            quizState?.let { quiz ->
                LazyColumn(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(quiz.questions) { question ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(4.dp, RoundedCornerShape(8.dp)),
                            shape = RoundedCornerShape(8.dp),
                            elevation = 4.dp
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = question.question.text,
                                    style = MaterialTheme.typography.h6,
                                    color = Azul
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                question.options.forEach { option ->
                                    val isSelected = selectedOptions[question.question.id] == option.id
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        RadioButton(
                                            selected = isSelected,
                                            onClick = {
                                                viewModel.selectOption(question.question.id, option.id)
                                            },
                                            colors = RadioButtonDefaults.colors(selectedColor = Azul)
                                        )
                                        Text(
                                            text = option.text,
                                            style = MaterialTheme.typography.body1,
                                            modifier = Modifier.padding(start = 8.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = {
//                                viewModel.answerQuizzRemote(quizId)
                                quizState?.let {
                                    viewModel.answerQuizzRemote(it.quiz.id.toString())

                                }
                                      },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Azul)
                        ) {
                            Text(
                                text = "Enviar Respuestas",
                                style = MaterialTheme.typography.button,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(state.value) {
        when {
            state.value.isLoading -> dialogState = SetupCustomDialogState.Loading()
            state.value.error != null -> dialogState = SetupCustomDialogState.Error(state.value.error?.uiMessage)
            state.value.info != null -> {
                dialogState = SetupCustomDialogState.Success("Respuestas enviadas correctamente")
                delay(1000)
                viewModel.cleanData()

                navController.popBackStack()
            }
        }
    }

    SetupCustomDialog(
        setupCustomDialogState = dialogState,
        showDialog = dialogState != SetupCustomDialogState.Default()
    ) {
        dialogState = SetupCustomDialogState.Default()
    }
}
