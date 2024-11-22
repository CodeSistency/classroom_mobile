package com.example.classroom.presentation.screens.course.posts.addPost

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classroom.data.remote.dto.courses.CourseRequestDto
import com.example.classroom.data.remote.dto.posts.PostRequestDto
import com.example.classroom.data.repository.RepositoryBundle
import com.example.classroom.domain.model.entity.Area
import com.example.classroom.domain.model.entity.LocalCourses
import com.example.classroom.domain.model.entity.LocalPost
import com.example.classroom.domain.model.entity.LocalUser
import com.example.classroom.domain.use_case.posts.CreatePostUseCase
import com.example.classroom.domain.use_case.posts.DeletePostUseCase
import com.example.classroom.domain.use_case.posts.GetPostsUseCase
import com.example.classroom.domain.use_case.posts.UpdatePostUseCase
import com.example.classroom.presentation.screens.course.AddCourse.states.AddCourseState
import com.example.classroom.presentation.screens.course.posts.addPost.composable.AddPostState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import proyecto.person.appconsultapopular.common.Resource
import timber.log.Timber

class AddPostViewModel(
    private val repositoryBundle: RepositoryBundle,
    private val createPostUseCase: CreatePostUseCase,
    private val updatePostUseCase: UpdatePostUseCase
) : ViewModel() {

    private val _userInfo = MutableStateFlow<LocalUser?>(null)
    val userInfo: StateFlow<LocalUser?> = _userInfo

    private val _statePost = MutableStateFlow(AddPostState())
    val statePost: StateFlow<AddPostState> = _statePost
    init {
        viewModelScope.launch {
            repositoryBundle.loginRepository.getUserInfoWithFlow()
                .firstOrNull()
                ?.firstOrNull()
                ?.let { _userInfo.value = it }

//            observeUserInput()
        }
    }

    var title = mutableStateOf("")
    var content = mutableStateOf("")
//    var courseId = mutableStateOf(0)
//    var authorId = mutableStateOf(0)

    // Validation States
    var titleError = mutableStateOf<String?>(null)
    var contentError = mutableStateOf<String?>(null)
//    var courseIdError = mutableStateOf<String?>(null)
//    var authorIdError = mutableStateOf<String?>(null)

    // Form Validity
    val isFormValid: Boolean
        get() = titleError.value == null &&
                contentError.value == null &&
//                courseIdError.value == null &&
//                authorIdError.value == null &&
                title.value.isNotBlank() &&
                content.value.isNotBlank()
//                courseId.value > 0 &&
//                authorId.value > 0

    // Validation Methods
    fun validateTitle() {
        titleError.value = if (title.value.isBlank()) "Title is required" else null
    }

    fun validateContent() {
        contentError.value = if (content.value.isBlank()) "Content is required" else null
    }

//    fun validateCourseId() {
//        courseIdError.value = if (courseId.value <= 0) "Course ID must be greater than zero" else null
//    }
//
//    fun validateAuthorId() {
//        authorIdError.value = if (authorId.value <= 0) "Author ID must be greater than zero" else null
//    }

    fun resetForm() {
        title.value = ""
        content.value = ""
//        courseId.value = 0
//        authorId.value = 0
    }

    fun fillForm(post: LocalPost) {
        title.value = post.title
        content.value = post.content
    }

    suspend fun executeCourseRequest(idPost: String?, courseId: String){

        var post = userInfo?.first()?.let {
           PostRequestDto(
               courseId = courseId.toInt(),
               title = title.value,
               authorId = it.idApi.toInt(),
               content = content.value,
           )
        }

        if (idPost != null){
            if (post != null) {
                updatePostUseCase(post).onEach { result ->
                    when(result){
                        is Resource.Error -> {
                            //Timber.tag("AUTH_VM").e("Error ${result.message?.uiMessage}")
                            Log.e("ACTIVITIES:", "Error ${result.message?.uiMessage}")
                            _statePost.value = AddPostState(error = result.message?.uiMessage)
                        }

                        is Resource.Loading -> {
                            Timber.tag("ACTIVITIES").e("is loading")
                            _statePost.value = AddPostState(isLoading = true)
                        }

                        is Resource.Success -> {
                            Timber.tag("ACTIVITIES_VM").e("success")
                            Log.e("ACTIVITIES:", "success")
                            _statePost.value = AddPostState(info = result.data)


                            _statePost.value.info?.let {
                                repositoryBundle.postsRepositoryImpl.updatePost(it)

                            }
                        }
                    }
                }.launchIn(viewModelScope)
            }
        }else {
            if (post != null) {
                createPostUseCase(post).onEach { result ->
                    when(result){
                        is Resource.Error -> {
                            //Timber.tag("AUTH_VM").e("Error ${result.message?.uiMessage}")
                            Log.e("ACTIVITIES:", "Error ${result.message?.uiMessage}")
                            _statePost.value = AddPostState(error = result.message?.uiMessage)
                        }

                        is Resource.Loading -> {
                            Timber.tag("ACTIVITIES").e("is loading")
                            _statePost.value = AddPostState(isLoading = true)
                        }

                        is Resource.Success -> {
                            Timber.tag("ACTIVITIES_VM").e("success")
                            Log.e("ACTIVITIES:", "success")
                            _statePost.value = AddPostState(info = result.data)


                            _statePost.value.info?.let {
                                repositoryBundle.postsRepositoryImpl.insertPost(it)
                            }
                        }
                    }
                }.launchIn(viewModelScope)
            }
        }

    }

}