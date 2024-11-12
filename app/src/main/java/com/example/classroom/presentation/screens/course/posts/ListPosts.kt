package com.example.classroom.presentation.screens.course.posts

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun ListPosts(viewModel: PostsViewModel, courseId: String) {
    val posts by viewModel.postsFlow.collectAsState()
    val postsState by viewModel.postsState.collectAsState()

    LaunchedEffect(key1 = posts, block = {
        Log.e("posts", posts.toString())
    })

    LaunchedEffect(true) {
        viewModel.fetchPosts(courseId)
    }

    when {
        postsState.isLoading -> {
            CircularProgressIndicator(modifier = Modifier.fillMaxSize())
        }
        postsState.error != null -> {
            Text(text = "Error: ${postsState.error}", color = Color.Red, modifier = Modifier.fillMaxSize())
        }
        postsState.info != null -> {
            if (postsState.info!!.isEmpty()){
                Text(text = "No hay publicaciones", modifier = Modifier.fillMaxSize())

            }else{
                LazyColumn {
                    items(posts) { post ->
                        CardPostItem(post = post)
                    }
                }
            }
        }
        else -> {

        }
    }
}