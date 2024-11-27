package com.example.classroom.presentation.screens.course.posts.addPost

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classroom.data.remote.dto.courses.CourseRequestDto
import com.example.classroom.data.remote.dto.posts.PostRequestDto
import com.example.classroom.data.repository.RepositoryBundle
import com.example.classroom.domain.model.entity.Area
import com.example.classroom.domain.model.entity.LocalCourses
import com.example.classroom.domain.model.entity.LocalPost
import com.example.classroom.domain.model.entity.LocalUser
import com.example.classroom.domain.use_case.cloud.UploadFileUseCase
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
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class AddPostViewModel(
    private val repositoryBundle: RepositoryBundle,
    private val createPostUseCase: CreatePostUseCase,
    private val updatePostUseCase: UpdatePostUseCase,
    private val uploadFileUseCase: UploadFileUseCase
) : ViewModel() {

    private val _userInfo = MutableStateFlow<LocalUser?>(null)
    val userInfo: StateFlow<LocalUser?> = _userInfo

    private val _statePost = MutableStateFlow(AddPostState())
    val statePost: StateFlow<AddPostState> = _statePost

    var selectedFileUri by mutableStateOf<Uri?>(null)

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

    fun resetState(){
        _statePost.value = AddPostState(info = null, error = null, isLoading = false)
    }
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

    fun getFileFromUri(context: Context, uri: Uri): File? {
        return try {
            // Get the file name from the URI
            val fileName = uri.lastPathSegment ?: "temp_file"

            // Create a temporary file in the cache directory
            val file = File(context.cacheDir, fileName)

            // Open an InputStream to read the content of the URI
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(file)

            // Copy the content of the URI to the temporary file
            inputStream?.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }

            file
        } catch (e: Exception) {
            e.printStackTrace()
            null // Return null if there was an error
        }
    }

    fun executeCourseRequest(idPost: String?, courseId: String, isFileUploadChecked: Boolean, context: Context) {
        viewModelScope.launch {
            try {
                var fileUrl: String? = null

                // Handle file upload if file upload is checked
                if (isFileUploadChecked && selectedFileUri != null) {
                    val file = getFileFromUri(context, selectedFileUri!!)
                    if (file != null) {

                        Log.e("fileurl", file.toString())

                        uploadFileUseCase(selectedFileUri!!, context).collect { result ->
                            when (result) {
                                is Resource.Error -> {
                                    Log.e("fileurl error", result.message?.uiMessage.toString())

                                    _statePost.value = AddPostState(error = "File upload error: ${result.message?.uiMessage}")
                                    return@collect
                                }
                                is Resource.Success -> {
                                    Log.e("fileurl", result.toString())
                                    fileUrl = result.data?.data?.fullPath

                                    result.data?.let {
                                        val postRequest = PostRequestDto(
                                            courseId = courseId.toInt(),
                                            title = title.value,
                                            content = content.value,
                                            authorId = userInfo.value?.idApi?.toInt() ?: 0,
                                            file = fileUrl // File URL if uploaded
                                        )

                                        if (idPost != null) {
                                            updatePostUseCase(postRequest).collect { result ->
                                                handleResult(result)
                                            }
                                        } else {
                                            createPostUseCase(postRequest).collect { result ->
                                                handleResult(result)
                                            }
                                        }
                                    }
                                }
                                is Resource.Loading -> {
                                    _statePost.value = AddPostState(isLoading = true)
                                }
                            }
                        }
                    }
                }else{
                    val postRequest = PostRequestDto(
                        courseId = courseId.toInt(),
                        title = title.value,
                        content = content.value,
                        authorId = userInfo.value?.idApi?.toInt() ?: 0,
                        file = fileUrl // File URL if uploaded
                    )

                    if (idPost != null) {
                        updatePostUseCase(postRequest).collect { result ->
                            handleResult(result)
                        }
                    } else {
                        createPostUseCase(postRequest).collect { result ->
                            handleResult(result)
                        }
                    }
                }


            } catch (e: Exception) {
                e.printStackTrace()
                _statePost.value = AddPostState(error = "Error: ${e.message}")
            }
        }
    }

    private fun handleResult(result: Resource<LocalPost>) {
        when (result) {
            is Resource.Success -> {
                _statePost.value = AddPostState(info = result.data)
            }
            is Resource.Error -> {
                _statePost.value = AddPostState(error = result.message?.uiMessage)
            }
            is Resource.Loading -> {
                _statePost.value = AddPostState(isLoading = true)
            }
        }
    }


}