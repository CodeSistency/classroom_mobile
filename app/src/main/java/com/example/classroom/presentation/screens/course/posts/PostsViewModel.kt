package com.example.classroom.presentation.screens.course.posts

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classroom.domain.model.entity.LocalPost
import com.example.classroom.domain.repository.PostsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class PostsViewModel(private val repository: PostsRepository) : ViewModel() {

    private val _postsState = MutableStateFlow(PostsState())
    val postsState: StateFlow<PostsState> = _postsState

    val posts: (String) -> StateFlow<List<LocalPost>> = { courseId ->
        repository.getPostsByCourse(courseId)
            .onEach { posts ->
                Log.e("LocalPostViewModel", "Posts fetched from DB: ${posts.size} posts found. id ${courseId}")
                _postsState.value = _postsState.value.copy(info = posts)
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    }

    fun insertPost(post: LocalPost) = viewModelScope.launch {
        repository.insertPost(post)
    }

    fun updatePost(post: LocalPost) = viewModelScope.launch {
        repository.updatePost(post)
    }

    fun deletePost(post: LocalPost) = viewModelScope.launch {
        repository.deletePost(post)
    }

//    fun fetchPosts(courseId: Int) = viewModelScope.launch {
//        _postsState.value = _postsState.value.copy(isLoading = true)
//        val result = repository.fetchPostsFromApi(courseId)
//        _postsState.value = if (result.isSuccess) {
//            _postsState.value.copy(isLoading = false, posts = result.getOrDefault(emptyList()), error = null)
//        } else {
//            _postsState.value.copy(isLoading = false, error = result.exceptionOrNull()?.message)
//        }
//    }
}
