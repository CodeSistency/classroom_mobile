package com.example.classroom.presentation.screens.activity.studentEvaluations

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.classroom.common.uiState.UiState
import com.example.classroom.domain.model.entity.LocalActivitySubmission
import com.example.classroom.domain.model.entity.LocalStudentEvaluation
import com.example.classroom.presentation.screens.activity.studentEvaluations.composable.EvaluationItem

@Composable
fun StudentsEvaluationsScreen(
    viewModel: StudentEvaluationsViewModel,
    courseId: String,
    studentId: String,
    navController: NavController
) {
    // Trigger data loading when the screen is first displayed
    LaunchedEffect(Unit) {
        viewModel.loadStudentEvaluations(courseId, studentId)
    }

    // Observe the UI state from the ViewModel
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect( true, block = {
        Log.e("uistate", uiState.toString())
        }
    )

    when (uiState) {
        is UiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is UiState.Success -> {
            val evaluations = (uiState as UiState.Success<List<LocalActivitySubmission>>).data
            if (evaluations.isEmpty()) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text("No hay evaluaciones disponibles", style = MaterialTheme.typography.subtitle1)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(evaluations) { evaluation ->
                        EvaluationItem(
                            evaluation = evaluation,
                            navController = navController,
                            idCourse = courseId,
                            idStudent = studentId
                        )
                    }
                }
            }
        }
        is UiState.Error -> {
            val errorMessage = (uiState as UiState.Error).message
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = errorMessage, color = MaterialTheme.colors.error)
            }
        }
    }
}
