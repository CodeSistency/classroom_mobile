package com.example.classroom.presentation.screens.course.posts

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classroom.data.repository.RepositoryBundle
import com.example.classroom.domain.model.entity.LocalPost
import com.example.classroom.domain.model.entity.LocalUser
import com.example.classroom.domain.model.entity.toLocal
import com.example.classroom.domain.repository.PostsRepository
import com.example.classroom.domain.use_case.posts.DeletePostUseCase
import com.example.classroom.domain.use_case.posts.GetPostsUseCase
import com.example.classroom.presentation.screens.activity.addActivity.states.GetActivitiesState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import proyecto.person.appconsultapopular.common.Resource


class PostsViewModel(
    private val repository: PostsRepository,
    private val getPostsUseCase: GetPostsUseCase,
    private val deletePostUseCase: DeletePostUseCase,
    private val repositoryBundle: RepositoryBundle,
) : ViewModel() {

    private val _userInfo = MutableStateFlow<LocalUser?>(null)
    val userInfo: StateFlow<LocalUser?> = _userInfo

    private val _postsState = MutableStateFlow(PostsState())
    val postsState: StateFlow<PostsState> = _postsState

    private val _deletePostState = MutableStateFlow(DeletePostState())
    val deletePostState: StateFlow<DeletePostState> = _deletePostState

    private val _postsFlow = MutableStateFlow<List<LocalPost>>(emptyList())
    val postsFlow: StateFlow<List<LocalPost>> = _postsFlow

    private val _filteredPostsFlow = MutableStateFlow<List<LocalPost>>(emptyList())
    val filteredPostsFlow: StateFlow<List<LocalPost>> = _filteredPostsFlow

    init {
        viewModelScope.launch {
            repositoryBundle.loginRepository.getUserInfoWithFlow()
                .firstOrNull()
                ?.firstOrNull()
                ?.let { _userInfo.value = it }

//            observeUserInput()
        }
    }
    fun fetchPosts(courseId: String) {
        viewModelScope.launch {
            repository.getPostsByCourse(courseId)
                .distinctUntilChanged()
                .onEach { posts ->
                    if (!posts.isNullOrEmpty()) {
                        _postsState.value = _postsState.value.copy(info = posts)
                        _postsFlow.value = posts
                    } else {
                        _postsState.value = _postsState.value.copy(info = emptyList())
                        _postsFlow.value = emptyList()
                    }
                }
                .launchIn(viewModelScope)
        }

//        viewModelScope.launch {
//            repository.getPostsByCourse(courseId)
//                .distinctUntilChanged()
//                .onEach { posts ->
//                    if (!posts.isNullOrEmpty()) {
//                        _postsState.value = _postsState.value.copy(info = posts)
//                        _postsFlow.value = posts
//                    }else{
//                        _postsState.value = _postsState.value.copy(info = emptyList())
//                        _postsFlow.value = emptyList()
//                    }
//                }
//                .launchIn(viewModelScope)
//        }
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

    suspend fun getPostsByCourseRemote(id: String) {
        getPostsUseCase(id).onEach { result ->
            when (result) {
                is Resource.Error -> _postsState.value = PostsState(error = result.message?.uiMessage)
                is Resource.Loading -> _postsState.value = PostsState(isLoading = true)
                is Resource.Success -> {
                    _postsState.value = PostsState(info = result.data)
                    _postsState.value.info?.let {
                        repository.updateListPosts(id, it)
//                        repositoryBundle.activitiesRepository.insertAllActivities(it)
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    suspend fun deletePostCourseRemote(id: String) {
        deletePostUseCase(id).onEach { result ->
            when (result) {
                is Resource.Error -> _deletePostState.value.copy(error = result.message?.uiMessage)
                is Resource.Loading -> _deletePostState.value.copy(isLoading = true, error = null)
                is Resource.Success -> {
                    _deletePostState.value.copy(isLoading = false, error = null, )
                    result.data?.let {

                        repositoryBundle.postsRepositoryImpl.deletePostById(id)
                    }


                }
            }
        }.launchIn(viewModelScope)
    }

    fun filterPosts(query: String) {
        _filteredPostsFlow.value = if (query.isBlank()) {
            _postsFlow.value
        } else {
            _postsFlow.value.filter { post ->
                post.title.contains(query, ignoreCase = true) ||
                        post.content.contains(query, ignoreCase = true)
            }
        }
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

    fun downloadFile(context: Context, url: String, fileName: String) {
        try {
            val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val uri = Uri.parse(url)

            val request = DownloadManager.Request(uri).apply {
                setTitle("Downloading $fileName")
                setDescription("File is being downloaded...")
                setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
            }

            downloadManager.enqueue(request)
            Toast.makeText(context, "Downloading $fileName...", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Failed to download file: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    fun cleanData (){
        _postsState.value = PostsState(null, false, null)
        _deletePostState.value = DeletePostState(null, false, null)
    }
}
