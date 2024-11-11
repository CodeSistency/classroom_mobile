package com.example.classroom.presentation.screens.submission.professor

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.classroom.presentation.screens.submission.SubmissionViewModel
import com.example.classroom.presentation.screens.submission.composables.ReviewSubmissionScreen

@Composable
fun SubmissionProfessorScreen(
    viewModel: SubmissionViewModel,
    activityId: String,
    studentId: String,
    courseId: String,
) {
    val context = LocalContext.current
    val submission = viewModel.currentSubmission

    LaunchedEffect(Unit) {
        viewModel.loadSubmission(activityId, studentId)
    }

    submission?.let { loadedSubmission ->
        ReviewSubmissionScreen(
            submission = loadedSubmission,
            onDownloadFile = { documentUrl ->
                viewModel.downloadAndOpenFile(context, documentUrl, fileName = "submission_${loadedSubmission.id}.pdf")
            },
            onGradeChange = { newGrade ->
                viewModel.updateGrade(newGrade)
            },
            onSubmitGrade = {
                viewModel.submitGrade()
            }
        )
    } ?: run {
        Text("Cargando la información de la actividad...", modifier = Modifier.padding(16.dp))
    }
}