package com.example.classroom.presentation.screens.course.student.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.classroom.presentation.theme.Azul

@Composable
fun TopBarStudentCourse(navController: NavController){
    Row(
        modifier = Modifier.fillMaxWidth().background(Azul),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = {
            navController.popBackStack()
        }) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
        }
        // Add edit icon like in the professor's presentation
        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = "Edit Course",
            tint = Color.White,
            modifier = Modifier.clickable {
                // Action on click, if needed
            }
        )
    }
}