package com.example.classroom.presentation.screens.activity.studentEvaluations

import android.annotation.SuppressLint
import android.service.voice.VoiceInteractionSession.ActivityId
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.classroom.common.uiState.UiState
import com.example.classroom.domain.model.entity.LocalActivitySubmission
import com.example.classroom.presentation.screens.activity.studentEvaluations.composable.EvaluationItem
import com.example.classroom.presentation.theme.Azul

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun StudentsEvaluationsScreen(
    viewModel: StudentEvaluationsViewModel,
    studentId: String,
    courseId: String,
    navController: NavController
) {
    LaunchedEffect(true) {
        viewModel.observeLocalEvaluations(courseId, studentId)
        viewModel.getActivitiesByStudent(courseId, studentId)
    }

    val uiState = viewModel.stateStudentEvaluations.value

    // Pull-to-refresh state
    val pullRefreshState = rememberPullRefreshState(
        refreshing = uiState.isLoading,
        onRefresh = {
            viewModel.getActivitiesByStudent(courseId, studentId)
        }
    )

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
                Text(text = "Evaluaciones", color = Color.White, fontSize = 16.sp)
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState) // Enable pull-to-refresh
        ) {
            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
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
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
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
                else -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            "No hay evaluaciones disponibles",
                            style = MaterialTheme.typography.subtitle1
                        )
                    }
                }
            }

            // PullRefreshIndicator to show loading at the top
//            PullRefreshIndicator(
//                refreshing = uiState.isLoading,
//                state = pullRefreshState,
//                modifier = Modifier.align(Alignment.TopCenter)
//            )
        }
    }
}

//@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
//@Composable
//fun StudentsEvaluationsScreen(
//    viewModel: StudentEvaluationsViewModel,
//    studentId: String,
//    courseId: String,
//    navController: NavController
//) {
//    // Trigger data loading when the screen is first displayed
//    LaunchedEffect(true) {
//        Log.e("triggers", "triggers")
//
//        viewModel.observeLocalEvaluations(courseId, studentId)
//        viewModel.getActivitiesByStudent(courseId, studentId)
//    }
//
//    val uiState = viewModel.stateStudentEvaluations.value
//
//    LaunchedEffect(uiState) {
//        Log.e("uiState", uiState.toString())
//    }
//
//    Scaffold(
//        topBar = {
//            Row(
//                modifier= Modifier.background(Azul).fillMaxWidth(),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                IconButton(onClick = { navController.popBackStack() }) {
//                    Icon(
//                        imageVector = Icons.Default.ArrowBack,
//                        contentDescription = null,
//                        tint = Color.White)
//                }
//                Spacer(modifier = Modifier.width(3.dp))
//                Text(text = "Evaluaciones", color = Color.White, fontSize = 16.sp)
//            }
//        }
//    ){
//        when {
//            uiState.isLoading -> {
//                Box(
//                    modifier = Modifier.fillMaxSize(),
//                    contentAlignment = Alignment.Center
//                ) {
//                    CircularProgressIndicator()
//                }
//            }
////        uiState.error != null -> {
////            val errorMessage = uiState.error.uiMessage ?: "An unknown error occurred"
////            Box(
////                modifier = Modifier.fillMaxSize(),
////                contentAlignment = Alignment.Center
////            ) {
////                Text(
////                    text = errorMessage,
////                    color = MaterialTheme.colors.error
////                )
////            }
////        }
//            uiState.info != null -> {
//                val evaluations = uiState.info.orEmpty()
//                if (evaluations.isEmpty()) {
//                    Box(
//                        contentAlignment = Alignment.Center,
//                        modifier = Modifier.fillMaxSize()
//                    ) {
//                        Text(
//                            "No hay evaluaciones disponibles",
//                            style = MaterialTheme.typography.subtitle1
//                        )
//                    }
//                } else {
//                    LazyColumn(
//                        modifier = Modifier.fillMaxSize(),
//                        contentPadding = PaddingValues(16.dp),
//                        verticalArrangement = Arrangement.spacedBy(12.dp)
//                    ) {
//                        items(evaluations) { evaluation ->
//                            EvaluationItem(
//                                evaluation = evaluation,
//                                navController = navController,
//                                idCourse = courseId,
//                                idStudent = studentId
//                            )
//                        }
//                    }
//                }
//            }
//            else -> {
//                Box(
//                    contentAlignment = Alignment.Center,
//                    modifier = Modifier.fillMaxSize()
//                ) {
//                    Text(
//                        "No hay evaluaciones disponibles",
//                        style = MaterialTheme.typography.subtitle1
//                    )
//                }
//            }
//        }
//
//    }
//
//}