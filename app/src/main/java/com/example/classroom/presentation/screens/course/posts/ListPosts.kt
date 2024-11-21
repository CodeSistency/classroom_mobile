package com.example.classroom.presentation.screens.course.posts

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import proyecto.person.appconsultapopular.common.shimmerEffects.ListShimmer

@Composable
fun ListPosts(viewModel: PostsViewModel, courseId: String, scope: CoroutineScope) {
    val posts by viewModel.postsFlow.collectAsState()
    val postsState by viewModel.postsState.collectAsState()

    LaunchedEffect(key1 = posts, block = {
        Log.e("posts", posts.toString())
    })

    LaunchedEffect(true) {
        viewModel.fetchPosts(courseId)
//        viewModel.getPostsByCourseRemote(courseId)
    }

    when {
        postsState.isLoading -> {
            ListShimmer(quantity = 10)
//            Column(modifier = Modifier.fillMaxSize(),
//                verticalArrangement = Arrangement.Center,
//                horizontalAlignment = Alignment.CenterHorizontally) {
//                CircularProgressIndicator()
//
//            }
        }
//        postsState.error != null -> {
//
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
//                    Text(text = "Ha ocurrido un error")
//                    Spacer(modifier = Modifier.height(10.dp))
//                    IconButton(onClick = {
//                        scope.launch {
//                            viewModel.getPostsByCourseRemote(courseId)
//                        }
//                    }) {
//                        Icon(Icons.Outlined.Sync, contentDescription = null)
//                    }
//                }
//            }
//
//        }
        postsState.info != null -> {
            if (postsState.info!!.isEmpty()){
                Column(
                    modifier = Modifier.fillMaxSize(),
                    Arrangement.Center,
                    Alignment.CenterHorizontally
                ) {
                    Column(
                        modifier = Modifier,
                        Arrangement.Center,
                        Alignment.CenterHorizontally
                    ) {
                        Text(text = "No hay publicaciones")
                        Spacer(modifier = Modifier.height(10.dp))
                        IconButton(onClick = {
                            scope.launch {
                                viewModel.getPostsByCourseRemote(courseId)
                            }
                        }) {
                            Icon(Icons.Outlined.Sync, contentDescription = null)
                        }
                    }
                }
            }else{
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(posts) { post ->
                        CardPostItem(post = post, viewModel, scope)
                    }
                }
            }
        }
        else -> {

        }
    }
}