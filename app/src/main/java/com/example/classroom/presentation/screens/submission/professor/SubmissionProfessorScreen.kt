package com.example.classroom.presentation.screens.submission.professor

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.classroom.common.composables.customDialogs.SetupCustomDialog
import com.example.classroom.common.composables.customDialogs.SetupCustomDialogState
import com.example.classroom.presentation.screens.submission.SubmissionViewModel
import com.example.classroom.presentation.screens.submission.composables.ReviewSubmissionScreen
import kotlinx.coroutines.delay

@Composable
fun SubmissionProfessorScreen(
    viewModel: SubmissionViewModel,
    activityId: String,
    studentId: String,
    courseId: String,
    navController: NavController
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
          viewModel = viewModel,
            navController = navController
        )
    } ?: run {
        Text("Cargando la información de la actividad...", modifier = Modifier.padding(16.dp))
    }


}