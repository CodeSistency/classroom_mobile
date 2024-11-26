package com.example.classroom.presentation.screens.submission.student

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.classroom.presentation.screens.submission.SubmissionViewModel
import com.example.classroom.presentation.screens.submission.composables.SubmissionForm

@Composable
fun SubmissionStudentScreen(
    viewModel: SubmissionViewModel,
    activityId: String,
    studentId: String,
    courseId: String,
    navController: NavController
){
    val context = LocalContext.current

    SubmissionForm(
        viewModel = viewModel,
        activityId = activityId,
        onSubmit = { fileUri, message ->
            viewModel.submitStudentResponse(
                context = context,
                activityId = activityId,
                fileUri = fileUri,
                message = message,
                onSubmissionSuccess = {},
                onSubmissionFailure = {},
                userId = studentId
            )
        },
        navController = navController
    )
}