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
import com.example.classroom.common.composables.RetryComponent.RetryComponent
import com.example.classroom.presentation.screens.activity.ActivityViewmodel
import com.example.classroom.presentation.screens.activity.addActivity.AddActivityViewModel
import com.example.classroom.presentation.screens.course.CourseViewmodel
import com.example.classroom.presentation.screens.home.HomeViewmodel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ListActivities(
    viewModel: ActivityViewmodel,
    courseViewmodel: CourseViewmodel,
    addActivityViewModel: AddActivityViewModel,
    scope: CoroutineScope,
    id: String,
    navController: NavController
) {
    val items by viewModel.filteredListActivitiesByCourseFlow.collectAsState(initial = listOf())
    val state by viewModel.stateGetActivities

    LaunchedEffect(true) {
            viewModel.getActivitiesLocalByCourse(id)

        Log.e("filtered activities", items.toString())


//        if (items.isEmpty() && id.isNotEmpty()) {
//            viewModel.getActivitiesLocalByCourse(id)
//        }
    }

    // Pull-to-refresh state
    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.isLoading,
        onRefresh = {
            scope.launch {
                viewModel.getActivitiesByCourse(id) // Refresh by fetching from the remote
            }
        }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState) // Enables pull-to-refresh
    ) {
        if (items.isEmpty() && !state.isLoading) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                RetryComponent(mensaje = "No hay actividades", onRetryClick = {

                    scope.launch {
                        viewModel.getActivitiesByCourse(id) // Manual refresh
                    }
                })

//                Text(text = "No hay actividades")
//                Spacer(modifier = Modifier.height(10.dp))
//                IconButton(onClick = {
//                    scope.launch {
//                        viewModel.getActivitiesByCourse(id) // Manual refresh
//                    }
//                }) {
//                    Icon(Icons.Outlined.Sync, contentDescription = null)
//                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 45.dp)
            ) {
                items(items) { activity ->
                    Box(modifier = Modifier.padding(vertical = 6.dp, horizontal = 2.dp)) {
                        CardActivity(
                            activity = activity,
                            msgDeleteBtn = "Eliminar",
                            msgDelete = "¿Estás seguro que deseas eliminar esta actividad?",
                            navController = navController,
                            action = {
                                scope.launch {
                                    viewModel.deleteActivity(activity.idApi)
                                }
                            },
                            viewModel = addActivityViewModel
                        )
                    }
                }
            }
        }

        // PullRefreshIndicator shows loading at the top
        PullRefreshIndicator(
            refreshing = state.isLoading,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

//@Composable
//fun ListActivities(viewModel: ActivityViewmodel, courseViewmodel: CourseViewmodel, addActivityViewModel: AddActivityViewModel, scope: CoroutineScope, id: String, navController: NavController) {
//
//    val items by viewModel.filteredListActivitiesByCourseFlow.collectAsState(initial = listOf())
//    LaunchedEffect(key1 = items, block = {
//        Log.e("Activities", items.toString())
//
//    })
//    LaunchedEffect(key1 = true, block = {
//        if (items.isEmpty() && id != null){
//            viewModel.getActivitiesLocalByCourse(id)
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
//                    Text(text = "No hay actividades")
//                    Spacer(modifier = Modifier.height(10.dp))
//                    IconButton(onClick = {
//                        scope.launch {
////                            courseViewmodel.getCourseByIdLocal(id)
////                            courseViewmodel.getUsersByCourseLocal(id)
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
//                        CardActivity(activity = it, msgDeleteBtn = "Eliminar",
//                            msgDelete = "¿Estás seguro que deseas eliminar esta actividad?",
//                            navController = navController,
//                            action = {
//                                scope.launch {
//                                    viewModel.deleteActivity(it.idApi)
//                                }
//                            },
//                            viewModel = addActivityViewModel
//                        )
//                    }
//                }
//            }
//        }
//    }
//}