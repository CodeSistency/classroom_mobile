package com.example.classroom.presentation.screens.course.student

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.classroom.presentation.screens.activity.ActivityViewmodel
import com.example.classroom.presentation.screens.course.CourseViewmodel
import com.example.classroom.presentation.screens.course.student.composables.CourseStudentPresentation
import com.example.classroom.presentation.screens.course.student.composables.TopBarStudentCourse

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CourseStudentScreen(activityViewmodel: ActivityViewmodel, courseViewmodel: CourseViewmodel, id: String, navController: NavController){
    Scaffold(
        topBar = {
            TopBarStudentCourse(navController)
        }
    ) {

        CourseStudentPresentation(viewModel = activityViewmodel, courseViewmodel = courseViewmodel, id)
    }

}