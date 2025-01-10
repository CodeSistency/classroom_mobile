package com.example.classroom.presentation.screens.course.profesor.composables

import android.util.Log
import android.widget.Space
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.classroom.presentation.navigation.Destination
import com.example.classroom.presentation.screens.activity.ActivityViewmodel
import com.example.classroom.presentation.screens.course.CourseViewmodel
import com.example.classroom.presentation.screens.home.HomeViewmodel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ListUsers(
    viewModel: ActivityViewmodel,
    courseViewmodel: CourseViewmodel,
    scope: CoroutineScope,
    id: String,
    navController: NavController
) {
    val items by courseViewmodel.filteredListUsersByCourseFlow.collectAsState(initial = listOf())
    val state = courseViewmodel.stateGetUsers.collectAsState()

    LaunchedEffect(true) {
        if (items.isEmpty() && id.isNotEmpty()) {
            courseViewmodel.getUsersByCourseLocal(id)
        }
    }

    // Pull-to-refresh state
    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.value.isLoading,
        onRefresh = {
            scope.launch {
                courseViewmodel.getUsersByCourseRemote(id) // Fetch users from remote source
                courseViewmodel.getUsersByCourseLocal(id)  // Load updated users from local DB
            }
        }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState) // Enables pull-to-refresh
    ) {
        if (items.isEmpty() && !state.value.isLoading) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "No hay cursos")
                Spacer(modifier = Modifier.height(10.dp))
                IconButton(onClick = {
                    scope.launch {
                        courseViewmodel.getUsersByCourseRemote(id)
                        courseViewmodel.getUsersByCourseLocal(id)
                        viewModel.getActivitiesByCourse(id)
                    }
                }) {
                    Icon(Icons.Outlined.Sync, contentDescription = null)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 5.dp)
            ) {
                items(items) { student ->
                    Box(modifier = Modifier.padding(vertical = 6.dp, horizontal = 2.dp)) {
                        CardStudent(
                            student = student,
                            msgDelete = "¿Estás seguro de retirar este estudiante?",
                            msgDeleteBtn = "Retirar",
                            navController = navController,
                            courseId = id,
                            action = {}
                        )
                    }
                }
            }
        }

        // PullRefreshIndicator to show loading at the top
        PullRefreshIndicator(
            refreshing = state.value.isLoading,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}
//@Composable
//fun ListUsers(viewModel: ActivityViewmodel, courseViewmodel: CourseViewmodel, scope: CoroutineScope, id: String, navController: NavController) {
//
//    val items by courseViewmodel.filteredListUsersByCourseFlow.collectAsState()
//    LaunchedEffect(key1 = items, block = {
//        Log.e("students ui", items.toString())
//
//    })
//
//
//    LaunchedEffect(key1 = true, block = {
//        if (items.isEmpty() && id != null){
//            viewModel.getActivitiesLocalByCourse(id)
////            courseViewmodel.getUsersByCourseLocal(id)
//        }
//    })
//
//    Box(modifier = Modifier){
//        if (items.isEmpty()){
//            Column(
//                modifier = Modifier.fillMaxSize(),
//                Arrangement.Center,
//                Alignment.CenterHorizontally
//            ) {
//                Column(
//                    modifier = Modifier,
//                    Arrangement.Center,
//                    Alignment.CenterHorizontally
//                ) {
//                    Text(text = "No hay cursos")
//                    Spacer(modifier = Modifier.height(10.dp))
//                    IconButton(onClick = {
//                        scope.launch {
////                            courseViewmodel.getCourseByIdLocal(id)
//                            courseViewmodel.getUsersByCourseRemote(id)
//                            courseViewmodel.getUsersByCourseLocal(id)
//                            viewModel.getActivitiesByCourse(id)
//                        }
//                      }) {
//                        Icon(Icons.Outlined.Sync, contentDescription = null)
//                    }
//                }
//            }
//        }else{
//            LazyColumn(
//                modifier = Modifier.fillMaxSize().padding(bottom = 5.dp)
//            ){
//                items(items){
//                    Box(modifier = Modifier.padding(vertical = 6.dp, horizontal = 2.dp)){
//                        CardStudent(student = it,
//                            msgDelete = "¿Estás seguro de retirar este estudiante?",
//                            msgDeleteBtn = "Retirar",
//                            navController = navController,
//                            courseId = id,
//                            action = {})
//                    }
//                }
//            }
//        }
//    }
//}