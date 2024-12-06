package com.example.classroom.presentation.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DrawerValue
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.classroom.common.composables.bottomNav.ScaffoldBottomNav
import com.example.classroom.presentation.navigation.Destination
import com.example.classroom.presentation.screens.course.AddCourse.AddCourseViewModel
import com.example.classroom.presentation.screens.home.composables.HomePresentation
import com.example.classroom.presentation.screens.home.composables.NotificationsDrawer
import com.example.classroom.presentation.screens.home.composables.SelectedOptionDialog
import com.example.classroom.presentation.screens.home.composables.TopBarHome
import com.example.classroom.presentation.theme.Azul
import kotlinx.coroutines.launch
import proyecto.person.appconsultapopular.common.SnackbarDelegate

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavController, viewmodel: HomeViewmodel, addCourseViewModel: AddCourseViewModel){
        val items = listOf(
        Destination.HOME,
        Destination.ACTIVITIES

    )
    val scope = rememberCoroutineScope()
    val snackbarHost = remember { SnackbarHostState() }
    val snackbarDelegate = remember { SnackbarDelegate() }
    val scaffoldState = rememberScaffoldState()
    val drawerState = rememberDrawerState(DrawerValue.Closed)


    snackbarDelegate.apply {
        snackbarHostState = scaffoldState.snackbarHostState
        coroutineScope = scope
    }
    var isDialogOpen by remember { mutableStateOf(false) }

    Scaffold(
        scaffoldState = scaffoldState,
//        snackbarHost = snackbarHost,
        topBar = { TopBarHome(viewmodel, navController, scope, drawerState = drawerState) },
        content = {
            HomePresentation(viewmodel, navController, addCourseViewModel)

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
        },
        drawerContent = {
            NotificationsDrawer(viewmodel, scope, onClose = {
                scope.launch { drawerState.close() }
            })
        }
    )

    if (isDialogOpen){
        SelectedOptionDialog(dismissDialog = { isDialogOpen = false },
            navController = navController,
            viewmodel, scope)
    }

}
