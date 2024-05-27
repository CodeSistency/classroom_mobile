package com.example.classroom.presentation.screens.course

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.navigation.NavController
import com.example.classroom.presentation.screens.activity.ActivityViewmodel
import com.example.classroom.presentation.screens.course.profesor.CourseProfesorScreen
import com.example.classroom.presentation.screens.course.student.CourseStudentScreen

@Composable
fun CourseScreen(
    id: String,
    email: String,
    isOwner: Boolean,
    viewModel: CourseViewmodel,
    activityViewmodel: ActivityViewmodel,
    navController: NavController,
    focusManager : FocusManager
){

    LaunchedEffect(key1 = true, block = {
        viewModel.getCourseById(id)
    })

    if (isOwner){
        CourseProfesorScreen(viewModel = activityViewmodel, courseViewmodel = viewModel, id = id, email = email, navController)
    }else{
        CourseStudentScreen(activityViewmodel = activityViewmodel, courseViewmodel = viewModel, id = id, navController)
    }
//    var isOwner = viewModel.isOwner.collectAsState(initial = null)

//    if (isOwner.value != null){
//        if (isOwner.value!!){
//            CourseProfesorScreen(viewModel = activityViewmodel, courseViewmodel = viewModel, id = id)
//        }else{
//            CourseStudentScreen(activityViewmodel = activityViewmodel, courseViewmodel = viewModel, id = id)
//        }
//    }else{
//        Box(modifier = Modifier){
//            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
//        }
//    }

}