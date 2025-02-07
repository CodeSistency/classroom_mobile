package com.example.classroom.presentation.screens.course.posts

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.classroom.common.composables.RetryComponent.RetryComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import proyecto.person.appconsultapopular.common.shimmerEffects.ListShimmer

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ListPosts(viewModel: PostsViewModel, courseId: String, scope: CoroutineScope) {
    val posts by viewModel.postsFlow.collectAsState()
    val postsState by viewModel.postsState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(true) {
        viewModel.fetchPosts(courseId)
    }
    LaunchedEffect(postsState.info) {
        Log.d("DEBUG", "Updated postsState: ${postsState.info}")
    }

    // Pull-to-refresh state linked to isLoading
    val pullRefreshState = rememberPullRefreshState(
        refreshing = postsState.isLoading,
        onRefresh = {
            scope.launch {
                viewModel.getPostsByCourseRemote(courseId)

//                viewModel.fetchPosts(courseId)
            }
        }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState) // Enables pull-to-refresh
    ) {
        when {
            postsState.isLoading -> {
                ListShimmer(quantity = 10)
            }
            postsState.info != null -> {
                if (postsState.info?.isEmpty() == true) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        RetryComponent(mensaje = "No hay publicaciones", onRetryClick = {
                            scope.launch {
                                viewModel.getPostsByCourseRemote(courseId)
                            }
                        })
                        
//                        Text(text = "No hay publicaciones")
//                        Spacer(modifier = Modifier.height(10.dp))
//                        IconButton(onClick = {
//                            scope.launch {
//                                viewModel.getPostsByCourseRemote(courseId)
//                            }
//                        }) {
//                            Icon(Icons.Outlined.Sync, contentDescription = null)
//                        }
                    }
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize().padding(bottom = 45.dp)) {
                        items(posts) { post ->
                            CardPostItem(post = post, viewModel, scope, context)
                        }
                    }
                }
            }
            else -> { Log.e("entro en el else", "else")
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    RetryComponent(mensaje = "No hay publicaciones", onRetryClick = {
                        scope.launch {
                            viewModel.getPostsByCourseRemote(courseId)
                        }
                    })

//                        Text(text = "No hay publicaciones")
//                        Spacer(modifier = Modifier.height(10.dp))
//                        IconButton(onClick = {
//                            scope.launch {
//                                viewModel.getPostsByCourseRemote(courseId)
//                            }
//                        }) {
//                            Icon(Icons.Outlined.Sync, contentDescription = null)
//                        }
                }

            }
        }

        // PullRefreshIndicator to show loading at the top
        PullRefreshIndicator(
            refreshing = postsState.isLoading,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}
//@Composable
//fun ListPosts(viewModel: PostsViewModel, courseId: String, scope: CoroutineScope) {
//    val posts by viewModel.postsFlow.collectAsState()
//    val postsState by viewModel.postsState.collectAsState()
//    val context = LocalContext.current
//
//    LaunchedEffect(key1 = posts, block = {
//        Log.e("posts", posts.toString())
//    })
//
//    LaunchedEffect(key1 = postsState, block = {
//        Log.e("postsState", postsState.toString())
//    })
//    LaunchedEffect(true) {
//        viewModel.fetchPosts(courseId)
////        viewModel.getPostsByCourseRemote(courseId)
//    }
//
//    when {
//        postsState.isLoading -> {
//            ListShimmer(quantity = 10)
////            Column(modifier = Modifier.fillMaxSize(),
////                verticalArrangement = Arrangement.Center,
////                horizontalAlignment = Alignment.CenterHorizontally) {
////                CircularProgressIndicator()
////
////            }
//        }
////        postsState.error != null -> {
////
////            Column(
////                modifier = Modifier.fillMaxSize(),
////                Arrangement.Center,
////                Alignment.CenterHorizontally
////            ) {
////                Column(
////                    modifier = Modifier,
////                    Arrangement.Center,
////                    Alignment.CenterHorizontally
////                ) {
////                    Text(text = "Ha ocurrido un error")
////                    Spacer(modifier = Modifier.height(10.dp))
////                    IconButton(onClick = {
////                        scope.launch {
////                            viewModel.getPostsByCourseRemote(courseId)
////                        }
////                    }) {
////                        Icon(Icons.Outlined.Sync, contentDescription = null)
////                    }
////                }
////            }
////
////        }
//        postsState.info != null -> {
//
//            Log.e("Post inside", postsState.toString())
//            if (postsState.info!!.isEmpty()){
//                Column(
//                    modifier = Modifier.fillMaxSize(),
//                    Arrangement.Center,
//                    Alignment.CenterHorizontally
//                ) {
//                    Column(
//                        modifier = Modifier,
//                        Arrangement.Center,
//                        Alignment.CenterHorizontally
//                    ) {
//                        Text(text = "No hay publicaciones")
//                        Spacer(modifier = Modifier.height(10.dp))
//                        IconButton(onClick = {
//                            scope.launch {
//                                viewModel.getPostsByCourseRemote(courseId)
//                            }
//                        }) {
//                            Icon(Icons.Outlined.Sync, contentDescription = null)
//                        }
//                    }
//                }
//            }else{
//                LazyColumn(modifier = Modifier.fillMaxSize()) {
//                    items(posts) { post ->
//                        CardPostItem(post = post, viewModel, scope, context)
//                    }
//                }
//            }
//        }
//        else -> {
//
//        }
//    }
//}