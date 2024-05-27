package com.example.classroom.presentation.screens.home.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.classroom.presentation.navigation.Destination
import com.example.classroom.presentation.screens.home.HomeViewmodel
import com.example.classroom.presentation.theme.Azul
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun TopBarHome(viewmodel: HomeViewmodel, navController: NavController, scope: CoroutineScope){
    Row(
        modifier = Modifier.fillMaxWidth().background(Azul),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = {
            scope.launch {
                viewmodel.logout()
                delay(200)
                navController.popBackStack()
                navController.navigate(Destination.LOGIN.screenRoute){
                    popUpTo(Destination.HOME.screenRoute){
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }
        }) {
            Icon(imageVector = Icons.Default.Menu, contentDescription = null, tint = Color.White)
        }
        IconButton(onClick = {

        }) {
            Icon(imageVector = Icons.Default.Notifications, contentDescription = null,  tint = Color.White)
        }
    }
}