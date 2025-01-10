package com.example.classroom.presentation.screens.course.student.composables

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.classroom.presentation.screens.activity.studentEvaluations.composable.EvaluationItem
import com.example.classroom.presentation.screens.course.CourseViewmodel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ListActivitiesSubmitted(
    viewModel: CourseViewmodel,
    navController: NavController,
    studentId: String,
    courseId: String
) {
    val scope = rememberCoroutineScope()

    // Trigger data loading when the screen is first displayed
    LaunchedEffect(true) {
        viewModel.observeLocalEvaluations(courseId, studentId)
        viewModel.getActivitiesByStudent(courseId, studentId)
    }

    val uiState = viewModel.stateStudentEvaluations.value

    // Pull-to-refresh state, using the uiState's isLoading directly
    val pullRefreshState = rememberPullRefreshState(
        refreshing = uiState.isLoading,
        onRefresh = {
            scope.launch {
                viewModel.getActivitiesByStudent(courseId, studentId)
            }
        }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState) // Add pull-to-refresh modifier
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
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "No hay evaluaciones disponibles")
                        Spacer(modifier = Modifier.height(10.dp))
                        IconButton(onClick = {
                            scope.launch {
                                viewModel.getActivitiesByStudent(courseId, studentId)
                            }
                        }) {
                            Icon(Icons.Outlined.Sync, contentDescription = null)
                        }
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
                            )
                        }
                    }
                }
            }
        }

        // PullRefreshIndicator shows the refresh progress at the top of the screen
        PullRefreshIndicator(
            refreshing = uiState.isLoading,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}
//@Composable
//fun ListActivitiesSubmitted(
//    viewModel: CourseViewmodel,
//    navController: NavController,
////    activtyId: String,
//    studentId: String,
//    courseId: String
//){
//
//    val scope = rememberCoroutineScope()
//
//    // Trigger data loading when the screen is first displayed
//
//    LaunchedEffect(true) {
//
//        viewModel.observeLocalEvaluations(courseId, studentId)
//        viewModel.getActivitiesByStudent(courseId, studentId)
//    }
//
//    val uiState = viewModel.stateStudentEvaluations.value
//
//    LaunchedEffect(uiState) {
//        Log.e("uiState", uiState.toString())
//
//    }
//
////    LaunchedEffect(key1 = true, block = {
////        viewModel.getActivitiesSubmitted(activtyId, studentId)
////    })
//
//
//    when {
//        uiState.isLoading -> {
//            Box(
//                modifier = Modifier.fillMaxSize(),
//                contentAlignment = Alignment.Center
//            ) {
//                CircularProgressIndicator()
//            }
//        }
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
//        uiState.info != null -> {
//            val evaluations = uiState.info.orEmpty()
//            if (evaluations.isEmpty()) {
//                Column(
//                    modifier = Modifier.fillMaxSize(),
//                    Arrangement.Center,
//                    Alignment.CenterHorizontally
//                ) {
//                    Column(
//                        modifier = Modifier,
//                        Arrangement.Center,
//                        Alignment.CenterHorizontally
//                    ) {
//                        Text(text = "No hay evaluaciones disponibles")
//                        Spacer(modifier = Modifier.height(10.dp))
//                        IconButton(onClick = {
//                            scope.launch {
//                                viewModel.getActivitiesByStudent(courseId, studentId)
//                            }
//                        }) {
//                            Icon(Icons.Outlined.Sync, contentDescription = null)
//                        }
//                    }
//                }
//
//            } else {
//                LazyColumn(
//                    modifier = Modifier.fillMaxSize(),
//                    contentPadding = PaddingValues(6.dp),
//                    verticalArrangement = Arrangement.spacedBy(12.dp)
//                ) {
//                    items(evaluations) { evaluation ->
//                        CardActivitySubmitted(
//                            evaluation = evaluation,
//                            navController = navController,
//
//                        )
//                    }
//                }
//            }
//        }
//    }
//}