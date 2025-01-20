package com.example.classroom.presentation.screens.home.composables

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Badge
import androidx.compose.material.BadgedBox
import androidx.compose.material.DrawerState
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
fun TopBarHome(
    viewmodel: HomeViewmodel,
    navController: NavController,
    scope: CoroutineScope,
    drawerState: DrawerState,
    onNavigate: () -> Unit
) {
    val unseenCount by viewmodel.unseenCount.collectAsState(initial = 0)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Azul),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = {
            onNavigate()
            Log.e("login", "executing")
//
//            navController.navigate(Destination.LOGIN.screenRoute){
//                launchSingleTop = true
//            }
//
//            scope.launch {
//                viewmodel.logout()
//            }
        }) {
            Icon(
                imageVector = Icons.Default.ArrowBackIos,
                contentDescription = null,
                tint = Color.White
            )
        }
        IconButton(onClick = {
            scope.launch { drawerState.open() }
        }) {
            BadgedBox(
                badge = {
                    if (unseenCount > 0) {
                        Badge { Text(unseenCount.toString()) }
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    }
}
