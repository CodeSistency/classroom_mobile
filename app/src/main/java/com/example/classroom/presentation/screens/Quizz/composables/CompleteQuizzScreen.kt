package com.example.classroom.presentation.screens.Quizz.composables

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.RadioButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType.Companion.Text
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.classroom.common.CustomButton.CustomButton
import com.example.classroom.common.CustomButton.NavigationButtonStyle
import com.example.classroom.common.composables.customDialogs.SetupCustomDialog
import com.example.classroom.common.composables.customDialogs.SetupCustomDialogState
import com.example.classroom.presentation.screens.Quizz.QuizzViewModel
import com.example.classroom.presentation.theme.Azul
import com.example.classroom.presentation.theme.AzulGradient
import kotlinx.coroutines.delay

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AnswerQuizScreen(viewModel: QuizzViewModel, quizId: String, navController: NavController) {
    val quizState by viewModel.quizState
    val state = viewModel.stateAnswerQuizz.collectAsState()
    var dialogState: SetupCustomDialogState by remember {
        mutableStateOf(SetupCustomDialogState.Default())
    }

    LaunchedEffect(Unit) {
        viewModel.loadQuiz(quizId.toInt())
    }

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
                    Text(text = "Responder Quizz", color = Color.White, fontSize = 16.sp)
                }
            }
        ){
            quizState?.let { quiz ->
                Column {
                    quiz.questions.forEach { question ->
                        Text(question.question.text)

                        question.options.forEach { option ->
                            val isSelected = viewModel.getSelectedOption(question.question.id) == option.id
                            Row {
                                RadioButton(
                                    selected = isSelected,
                                    onClick = {
                                        viewModel.selectOption(question.question.id, option.id)
                                    }
                                )
                                Text(option.text)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    CustomButton(
                        text = "Enviar",
                        color1 = Azul,
                        color2 = AzulGradient,
                        style = NavigationButtonStyle.SolidGradient,
                        onClick = {
                            viewModel.answerQuizzRemote(quizId)
                        })

                }
            }

        }
    }

    LaunchedEffect(key1 = state.value, block = {
        when{
            state.value.isLoading -> {
                dialogState = SetupCustomDialogState.Loading()
            }
            state.value.error != null -> {
                dialogState = SetupCustomDialogState.Error(state.value.error?.uiMessage)
            }

            else -> {
                if (state.value.info != null){
                    dialogState = SetupCustomDialogState.Success(message = "Se ha creado la actividad exitosamente")
                    delay(1000)
                    navController.popBackStack()
//                    viewModel.resetForm()
                }
            }
        }
    })

    SetupCustomDialog(setupCustomDialogState = dialogState, showDialog = dialogState != SetupCustomDialogState.Default()) {
        dialogState = SetupCustomDialogState.Default()
    }

}