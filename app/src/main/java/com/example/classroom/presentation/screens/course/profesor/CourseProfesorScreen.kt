package com.example.classroom.presentation.screens.course.profesor

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.classroom.presentation.screens.activity.ActivityViewmodel
import com.example.classroom.presentation.screens.course.CourseViewmodel
import com.example.classroom.presentation.screens.course.profesor.composables.CourseProfessorPresentation
import com.example.classroom.presentation.screens.course.profesor.composables.TopBarProfessor
import com.example.classroom.presentation.screens.course.profesor.composables.SelectedOptionDialog
import com.example.classroom.presentation.theme.Azul

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun CourseProfesorScreen(viewModel: ActivityViewmodel, courseViewmodel: CourseViewmodel, id: String, navController: NavController){
    Scaffold(
        topBar = {
            TopBarProfessor(navController = navController)
        }
    ) {
        CourseProfessorPresentation(viewModel = viewModel, courseViewmodel = courseViewmodel, id = id, navController)
        var isDialogOpen by remember { mutableStateOf(false) }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomEnd
                ) {
                    // Your main content goes here
                    FloatingActionButton(
                        modifier = Modifier.padding(bottom = 16.dp, end = 16.dp), // Add padding
                        onClick = {
                            isDialogOpen = true
                        },
                        backgroundColor = Azul
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Carta",
                            tint = Color.White
                        )
                    }
                }
        if (isDialogOpen){
            SelectedOptionDialog(dismissDialog = { isDialogOpen = false }, navController = navController)
        }
    }
}