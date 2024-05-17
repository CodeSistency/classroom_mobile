package com.example.classroom.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.classroom.presentation.navigation.Destination
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(navController: NavHostController) {
    LaunchedEffect(key1 = true, block = {
        //AQUI VA LA LOGICA DE A DONDE VA A NAVEGAR
        delay(1000)
        navController.popBackStack()
        navController.navigate(Destination.LOGIN.screenRoute)
    })
    Splash()
}

@Composable
fun Splash() {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //Image(modifier = Modifier.size(width = 250.dp, height = 250.dp), painter = painterResource(id = R.drawable.ic_consulta_popular), contentDescription = "logo")
    }
}
