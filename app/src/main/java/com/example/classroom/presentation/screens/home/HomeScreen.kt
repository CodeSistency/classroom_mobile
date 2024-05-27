package com.example.classroom.presentation.screens.home

import androidx.compose.material.SnackbarHostState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.example.classroom.common.bottomNav.ScaffoldBottomNav
import com.example.classroom.presentation.navigation.Destination
import com.example.classroom.presentation.screens.home.composables.HomePresentation
import com.example.classroom.presentation.screens.home.composables.SelectedOptionDialog
import com.example.classroom.presentation.screens.home.composables.TopBarHome
import proyecto.person.appconsultapopular.common.SnackbarDelegate

@Composable
fun HomeScreen(navController: NavController, viewmodel: HomeViewmodel){
        val items = listOf(
        Destination.HOME,
        Destination.ACTIVITIES

    )
    val scope = rememberCoroutineScope()
    val snackbarHost = remember { SnackbarHostState() }
    val snackbarDelegate = remember { SnackbarDelegate() }
    val scaffoldState = rememberScaffoldState()

    snackbarDelegate.apply {
        snackbarHostState = scaffoldState.snackbarHostState
        coroutineScope = scope
    }
    var isDialogOpen by remember { mutableStateOf(false) }

    ScaffoldBottomNav(
        scaffoldState = scaffoldState,
        snackbarDelegate = snackbarDelegate,
        snackbarHost = snackbarHost,
        navController = navController,
        topBar = { TopBarHome(viewmodel, navController, scope) },
        content = {
                    HomePresentation(viewmodel, navController)
                  },
        items = items,
        isFloatingAction = true,
        action = {
            isDialogOpen = true
        })

    if (isDialogOpen){
        SelectedOptionDialog(dismissDialog = { isDialogOpen = false },
            navController = navController,
            viewmodel, scope)
    }
}
