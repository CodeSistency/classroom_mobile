package com.example.classroom.presentation.screens.home.composables

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.classroom.presentation.screens.home.HomeViewmodel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun ListMyCourses(
    viewModel: HomeViewmodel,
    scope: CoroutineScope,
    navController: NavController,
    email: String,
){
    val items =  viewModel.filteredListMyCoursesFLow.collectAsState(initial = listOf())

    Box(modifier = Modifier){
        if (items.value.isEmpty()){
            Column(
                modifier = Modifier,
                Arrangement.Center,
                Alignment.CenterHorizontally
            ) {
                Column {
                    Text(text = "No hay cursos")
                    Spacer(modifier = Modifier.height(10.dp))
                    IconButton(onClick = {
                        scope.launch {
                            if ( viewModel.userInfo.first() != null)
                            viewModel.getCourses(
                               viewModel.userInfo.first()!!.idApi
                            )
                        }
                    }) {
                        Icon(Icons.Outlined.Sync, contentDescription = null)
                    }
                }
            }
        }else{
            LazyColumn(
                modifier = Modifier.padding(bottom = 95.dp)
            ){
                items(items.value){
                    Box(modifier = Modifier.padding(vertical = 6.dp, horizontal = 2.dp)){

                        CardCourses(course = it,
                            msgDelete = "¿Estás seguro de eliminar tu clase?",
                            msgDeleteBtn = "Eliminar", isOwner = true, action = {},
                            navController = navController,
                            email = email)
                    }

                }
            }
        }

    }
}