package com.example.classroom.presentation.screens.course.student.composables

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.classroom.presentation.screens.activity.studentEvaluations.composable.EvaluationItem
import com.example.classroom.presentation.screens.course.CourseViewmodel

@Composable
fun ListActivitiesSubmitted(
    viewModel: CourseViewmodel,
    navController: NavController,
    courseId: String,
    studentId: String
){

    // Trigger data loading when the screen is first displayed

    LaunchedEffect(true) {

        viewModel.observeLocalEvaluations(courseId, studentId)
        viewModel.getActivitiesByStudent(courseId, studentId)
    }

    val uiState = viewModel.stateStudentEvaluations.value

    LaunchedEffect(uiState) {
        Log.e("uiState", uiState.toString())

    }


    when {
        uiState.isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
//        uiState.error != null -> {
//            val errorMessage = uiState.error.uiMessage ?: "An unknown error occurred"
//            Box(
//                modifier = Modifier.fillMaxSize(),
//                contentAlignment = Alignment.Center
//            ) {
//                Text(
//                    text = errorMessage,
//                    color = MaterialTheme.colors.error
//                )
//            }
//        }
        uiState.info != null -> {
            val evaluations = uiState.info.orEmpty()
            if (evaluations.isEmpty()) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        "No hay evaluaciones disponibles",
                        style = MaterialTheme.typography.subtitle1
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(6.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(evaluations) { evaluation ->
                        CardActivitySubmitted(
                            evaluation = evaluation,
                            navController = navController,
                            idCourse = courseId,
                            idStudent = studentId
                        )
                    }
                }
            }
        }
    }
}