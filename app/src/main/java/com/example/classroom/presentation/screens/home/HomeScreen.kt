package com.example.classroom.presentation.screens.home

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.classroom.common.bottomNav.ScaffoldBottomNav
import com.example.classroom.presentation.navigation.Destination
import com.example.classroom.presentation.screens.home.composables.TopBarHome

@Composable
fun HomeScreen(navController: NavController){
        val items = listOf(
        Destination.HOME,
        Destination.REGISTRO

    )
    ScaffoldBottomNav(
        navController = navController,
        topBar = { TopBarHome() },
        content = {

                  },
        items = items)
}
