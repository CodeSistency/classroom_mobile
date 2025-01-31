package com.example.classroom.presentation.screens.submission.student

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.classroom.common.composables.customDialogs.SetupCustomDialog
import com.example.classroom.common.composables.customDialogs.SetupCustomDialogState
import com.example.classroom.presentation.screens.submission.SubmissionViewModel
import com.example.classroom.presentation.screens.submission.composables.SubmissionForm
import kotlinx.coroutines.delay

@Composable
fun SubmissionStudentScreen(
    viewModel: SubmissionViewModel,
    activityId: String,
    studentId: String,
    courseId: String,
    navController: NavController
){
    val context = LocalContext.current
    var dialogState: SetupCustomDialogState by remember {
        mutableStateOf(SetupCustomDialogState.Default())
    }
    var state = viewModel.stateSendActivity.collectAsState()

    SubmissionForm(
        viewModel = viewModel,
        activityId = activityId,
        onSubmit = { fileUri, message ->
            viewModel.submitStudentResponse(
                context = context,
                activityId = activityId,
                fileUri = fileUri,
                message = message,
                onSubmissionSuccess = {
                    dialogState = SetupCustomDialogState.Success("Se ha subido correctamente la evaluacion")
                },
                onSubmissionFailure = { message->
                    dialogState = SetupCustomDialogState.Error(message)

                },
                userId = studentId,
                idCourse = courseId,
                useSupabase = false
            )
        },
        navController = navController
    )

//    LaunchedEffect(key1 = state.value, block = {
//        Log.e("POST STATE", state.value.toString())
//        when{
//            state.value.isLoading -> {
//                dialogState = SetupCustomDialogState.Loading()
//            }
//
//        }
//    })
    LaunchedEffect(key1 = state.value, block = {
        when{
            state.value.isLoading -> {
                dialogState = SetupCustomDialogState.Loading()
                viewModel.cleanData()
            }
            state.value.error != null -> {
                dialogState = SetupCustomDialogState.Error(state.value.error!!.uiMessage)
                viewModel.cleanData()
            }

            else -> {
                if (state.value.info != null){
                    dialogState = SetupCustomDialogState.Success(message = "Se ha subido la evaluacion exitosamente")
                    delay(1000)
                    navController.popBackStack()
                    viewModel.cleanData()
                }
            }
        }
    })


    SetupCustomDialog(setupCustomDialogState = dialogState, showDialog = dialogState != SetupCustomDialogState.Default()) {
        dialogState = SetupCustomDialogState.Default()
    }


}