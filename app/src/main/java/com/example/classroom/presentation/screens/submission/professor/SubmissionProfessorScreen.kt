package com.example.classroom.presentation.screens.submission.professor

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
    val submission by viewModel.currentSubmission // Observe the state directly

    LaunchedEffect(Unit) {
        viewModel.loadSubmission(activityId, studentId)
    }

    LaunchedEffect(key1 = submission, block = {
        Log.e("submission Review", submission.toString())
    })

    submission?.let { loadedSubmission ->
        ReviewSubmissionScreen(
            submission = loadedSubmission,
            onDownloadFile = { documentUrl ->
                viewModel.downloadAndOpenFile(context, documentUrl, fileName = "submission_${loadedSubmission.id}.pdf")
            },
          viewModel = viewModel
        )
    } ?: run {
        Text("Cargando la información de la actividad...", modifier = Modifier.padding(16.dp))
    }
}