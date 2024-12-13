package com.example.classroom.presentation.screens.home.composables

import android.util.Log
import android.widget.Space
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.classroom.common.composables.card.ActionIcon
import com.example.classroom.common.composables.card.SwipeableItemWithActions
import com.example.classroom.common.composables.lists.PaginatedList
import com.example.classroom.presentation.screens.course.AddCourse.AddCourseViewModel
import com.example.classroom.presentation.screens.home.HomeViewmodel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun ListCourses(
    viewModel: HomeViewmodel,
    addCourseViewModel: AddCourseViewModel,
    scope: CoroutineScope,
    navController: NavController,
    email: String,
) {

    val context = LocalContext.current
    // Initialize the revealed states list, it will dynamically update as new items are paginated
    val revealedStates = remember { mutableStateListOf<Boolean>() }

    // Pagination state
    val paginationState by viewModel.coursesPaginationState.collectAsState()

    // Update the revealedStates list whenever new items are loaded
    LaunchedEffect(paginationState.items.size) {
        // Append false for each new item loaded from the pagination
        val newItemsCount = paginationState.items.size
        val currentSize = revealedStates.size
        if (newItemsCount > currentSize) {
            revealedStates.addAll(List(newItemsCount - currentSize) { false })
        }
    }

    PaginatedList(
        initialItems = paginationState.items,
        loadItems = { page, pageSize ->
            viewModel.loadItemsCourses(page, pageSize)
        },
        pageSize = 20,  // Adjust the page size as necessary
        onRenderItem = { course, index ->
            SwipeableItemWithActions(
                isRevealed = revealedStates.getOrElse(index) { false },
                actions = {
                    // Action button logic here
                    ActionIcon(
                        onClick = {
                            Toast.makeText(context, "Course deleted.", Toast.LENGTH_SHORT).show()
                            revealedStates[index] = false // Hide actions when an action is performed
                        },
                        backgroundColor = Color.Red,
                        icon = Icons.Default.Delete,
                        modifier = Modifier.fillMaxHeight()
                    )
                    ActionIcon(
                        onClick = {
                            Toast.makeText(context, "Course sent email.", Toast.LENGTH_SHORT).show()
                            revealedStates[index] = false // Hide actions
                        },
                        backgroundColor = Color.Yellow,
                        icon = Icons.Default.Email,
                        modifier = Modifier.fillMaxHeight()
                    )
                    ActionIcon(
                        onClick = {
                            Toast.makeText(context, "Course shared.", Toast.LENGTH_SHORT).show()
                            revealedStates[index] = false // Hide actions
                        },
                        backgroundColor = Color.Magenta,
                        icon = Icons.Default.Share,
                        modifier = Modifier.fillMaxHeight()
                    )
                },
            ) {

                CardCourses(
                    course = course,
                    msgDelete = "¿Estás seguro de eliminar este curso?",
                    msgDeleteBtn = "Eliminar",
                    isOwner = false,
                    action = {
                        scope.launch {
                            viewModel.deleteCourse(course.idApi)
                        }
                    },
                    navController = navController,
                    email = email,
                    viewModel = addCourseViewModel
                )
            }
        },
        modifier = Modifier.fillMaxSize()
    )

//    val items = viewModel.filteredListCoursesFlow.collectAsState(initial = listOf())
//
//    Box(modifier = Modifier){
//        if (items.value.isEmpty()){
//            Column(
//                modifier = Modifier.fillMaxSize(),
//                Arrangement.Center,
//                Alignment.CenterHorizontally
//            ) {
//                Column(
//                    modifier = Modifier,
//                    Arrangement.Center,
//                    Alignment.CenterHorizontally
//                ) {
//                    Text(text = "No hay cursos")
//                    Spacer(modifier = Modifier.height(10.dp))
//                    IconButton(onClick = {
//                        scope.launch {
//                            if ( viewModel.userInfo.first() != null)
//                                viewModel.getCourses(
//                                    viewModel.userInfo.first()!!.idApi
//                                )
//                        }
//                    }) {
//                        Icon(Icons.Outlined.Sync, contentDescription = null)
//                    }
//                }
//            }
//        }else{
//            LazyColumn(
//                modifier = Modifier.fillMaxSize().padding(bottom = 95.dp)
//            ){
//                items(items.value){
//                    Box(modifier = Modifier.padding(vertical = 6.dp, horizontal = 2.dp)){
//                        CardCourses(course = it,
//                            msgDelete = "¿Estás seguro de eliminar tu clase?",
//                            msgDeleteBtn = "Eliminar",
//                            isOwner = false,
//                            action = {
//                                     scope.launch {
//                                         viewModel.deleteCourse(it.idApi)
//                                     }
//                            },
//                            navController = navController,
//                            email= email,
//                            viewModel = addCourseViewModel
//                            )
//                    }
//
//                }
//            }
//        }
//
//    }

}