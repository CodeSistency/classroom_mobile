package com.example.classroom.presentation.screens.course.profesor.composables

import android.util.Log
import android.widget.Space
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.classroom.presentation.screens.activity.ActivityViewmodel
import com.example.classroom.presentation.screens.course.CourseViewmodel
import com.example.classroom.presentation.screens.home.HomeViewmodel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun ListActivities(viewModel: ActivityViewmodel, courseViewmodel: CourseViewmodel, scope: CoroutineScope, id: String, navController: NavController) {

    val items by viewModel.filteredListActivitiesByCourseFLow.collectAsState(initial = listOf())
    LaunchedEffect(key1 = items, block = {
        Log.e("Activities", items.toString())

    })
    LaunchedEffect(key1 = true, block = {
        if (items.isEmpty() && id != null){
            viewModel.getActivitiesLocalByCourse(id)
        }
    })

    Box(modifier = Modifier){
        if (items.isEmpty()){
            Column(
                modifier = Modifier,
                Arrangement.Center,
                Alignment.CenterHorizontally
            ) {
                Column {
                    Text(text = "No hay cursos")
                    Spacer(modifier = Modifier.height(10.dp))
                    IconButton(onClick = {
                        scope.launch {
                            courseViewmodel.getCourseByIdLocal(id)
                            courseViewmodel.getUsersByCourseRemote(id)
                            courseViewmodel.getUsersByCourseLocal(id)
                            viewModel.getActivitiesByCourse(id)
                        }
                      }) {
                        Icon(Icons.Outlined.Sync, contentDescription = null)
                    }
                }
            }
        }else{
            LazyColumn(
                modifier = Modifier.padding(bottom = 5.dp)
            ){
                items(items){
                    Box(modifier = Modifier.padding(vertical = 6.dp, horizontal = 2.dp)){
                        CardActivity(activity = it, msgDeleteBtn = "Eliminar",
                            msgDelete = "¿Estás seguro que deseas eliminar esta actividad?",
                            navController = navController,
                            action = {

                            }
                        )
                    }
                }
            }
        }
    }
}