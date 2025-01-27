package com.example.classroom.presentation.screens.course.profesor.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.classroom.presentation.screens.course.CourseViewmodel
import com.example.classroom.presentation.theme.Azul

@Composable
fun TopBarProfessor(navController: NavController, courseViewmodel: CourseViewmodel){
    val courseInfo = courseViewmodel.courseFlow.collectAsState(initial = null)

    Row(
        modifier = Modifier.fillMaxWidth().background(Azul),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = {
            navController.popBackStack()
        }) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
        }

        courseInfo.value.let {
            if (it != null){
                if (it.verified){

                    // Your main content goes here
                    //Aqui es donde se va a poder editar el curso
//                    IconButton(onClick = {
//
//                    }) {
//                        Icon(imageVector = Icons.Default.Edit, contentDescription = null,  tint = Color.White)
//                    }

                }
            }
        }



    }
}