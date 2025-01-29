package com.example.classroom.presentation.screens.submission

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classroom.common.apiUtils.retryOperation
import com.example.classroom.common.uiState.UiState
import com.example.classroom.data.remote.dto.evaluations.reviewEvaluationDto.ReviewEvaluationRequestDto
import com.example.classroom.data.remote.dto.evaluations.sendEvaluationRequestDto.SendEvaluationRequestDto
import com.example.classroom.data.repository.RepositoryBundle
import com.example.classroom.domain.model.entity.Area
import com.example.classroom.domain.model.entity.LocalActivitySubmission
import com.example.classroom.domain.model.entity.Status
import com.example.classroom.domain.model.entity.toCoursesLocal
import com.example.classroom.domain.use_case.cloud.UploadFileUseCase
import com.example.classroom.domain.use_case.evaluations.professorReviewsActivityUseCase.ProfessorReviewsActivityUseCase
import com.example.classroom.domain.use_case.evaluations.studentSendActivityUseCase.StudentSendActivityUseCase
import com.example.classroom.presentation.screens.activity.studentEvaluations.states.StudentEvaluationsState
import com.example.classroom.presentation.screens.home.states.CourseState
import com.example.classroom.presentation.screens.submission.states.ReviewActivityState
import com.example.classroom.presentation.screens.submission.states.SendActivityState
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import proyecto.person.appconsultapopular.common.Resource
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.time.Instant
import java.util.Date

class SubmissionViewModel(
    val repositoryBundle: RepositoryBundle,
    val professorReviewsActivityUseCase: ProfessorReviewsActivityUseCase,
    val studentSendActivityUseCase: StudentSendActivityUseCase,
    val uploadFileUseCase: UploadFileUseCase,
) : ViewModel() {

    var message = mutableStateOf("")
    var grade = mutableStateOf(0.0)


    // Estados de validación
    var messageError = mutableStateOf<String?>(null)
    var gradeError = mutableStateOf<String?>(null)

    private val _stateSendActivity = MutableStateFlow(SendActivityState())
    val stateSendActivity: StateFlow<SendActivityState> = _stateSendActivity

    private val _stateReviewActivity = MutableStateFlow(ReviewActivityState())
    val stateReviewActivity: StateFlow<ReviewActivityState> = _stateReviewActivity

    // Make currentSubmission a mutable state
    private val _currentSubmission = mutableStateOf<LocalActivitySubmission?>(null)
    val currentSubmission: State<LocalActivitySubmission?> = _currentSubmission

    // Load submission data for the professor to review
    fun loadSubmission(activityId: String, studentId: String) {
        viewModelScope.launch {
            val submissions = repositoryBundle.submissionsRepository
                .getSubmissionsForStudent(activityId, studentId)
                .first() // Get the first list emitted from Flow

            _currentSubmission.value = submissions.firstOrNull() // Safely get the first item or null if empty

            if (_currentSubmission.value == null) {
                Log.e("loadSubmission", "No submissions found for activityId: $activityId, studentId: $studentId")
                // Handle the case where there are no submissions (e.g., set a default value or notify the UI)
            } else {
                Log.d("loadSubmission", "Loaded submission: ${_currentSubmission.value}")
            }
        }
    }



    // Update the grade locally
    fun updateGrade(newGrade: Double) {
        _currentSubmission.value = _currentSubmission.value?.copy(grade = newGrade)
    }

    // Submit the updated grade to the repository
    fun submitGrade() {
        _currentSubmission.value?.let { submission ->
            viewModelScope.launch {
                repositoryBundle.submissionsRepository.addOrUpdateSubmission(submission)
            }
        }
    }



    fun submitStudentResponse(
        context: Context,
        activityId: String,
        idCourse: String,
        userId: String,
        fileUri: Uri,
        message: String,
        useSupabase: Boolean,
        onSubmissionSuccess: () -> Unit,
        onSubmissionFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                var fileUrl: String? = null

                // Step 1: Retry File Upload
                try {
                    fileUrl = retryOperation(times = 3, delayMillis = 2000L) {
                        var resultUrl: String? = null
                        uploadFileUseCase(fileUri, context, useSupabase).collect { result ->
                            when (result) {
                                is Resource.Loading -> {

                                    Log.e("Loading", result.data.toString())

                                    _stateSendActivity.value = SendActivityState(isLoading = true)
                                }
                                is Resource.Error -> {
                                    Log.e("Error", result.data.toString())

                                    throw Exception("${result.message?.uiMessage}")
                                }
                                is Resource.Success -> {
                                    Log.e("success", result.data.toString())

                                    resultUrl = result.data?.data?.fullPath
                                }
                                else -> {}
                            }
                        }
                        resultUrl ?: throw Exception("File upload failed: URL is null or empty")
                    }
                    Log.e("submitStudentResponse", "File uploaded successfully: $fileUrl")
                } catch (e: Exception) {
                    onSubmissionFailure("File upload failed after retries: ${e.message}")
                    return@launch
                }

                // Step 2: Retry Activity Submission
                try {
                    retryOperation(times = 3, delayMillis = 2000L) {
                        studentSendActivityUseCase(
                            SendEvaluationRequestDto(
                                userId = userId.toInt(),
                                activityId = activityId.toInt(),
                                message = message,
                                document = fileUrl!!
                            ),
                            idCourse
                        ).collect { result ->
                            when (result) {
                                is Resource.Error -> {
                                    _stateSendActivity.value = SendActivityState(info = null, isLoading = false, error = result.message)

                                    throw Exception("${result.message?.uiMessage}")
                                }
                                is Resource.Loading -> {
//                                    _stateSendActivity.value = SendActivityState(isLoading = true)
                                }
                                is Resource.Success -> {
                                    Log.e("submitStudentResponse", "Activity submitted successfully.")
                                    result.data?.let {
                                        _stateSendActivity.value = SendActivityState(info = result.data, isLoading = false, error = null)
                                        repositoryBundle.submissionsRepository.addOrUpdateSubmission(submission = result.data)
                                        delay(500)
                                        onSubmissionSuccess()

                                    }
                                }
                                else -> {}
                            }
                        }
                    }
                } catch (e: Exception) {
                    onSubmissionFailure("${e.message}")
                    return@launch
                }

            } catch (e: Exception) {
                e.printStackTrace()
                onSubmissionFailure("${e.message}")
            }
        }
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

    fun cleanData(){
        _stateReviewActivity.value = ReviewActivityState(false, null, null)
        _stateSendActivity.value = SendActivityState(false, null, null)

    }
    @SuppressLint("Range")
    fun downloadAndOpenFile(
        context: Context,
        documentUrl: String,
        fileName: String = "document",
        onProgress: (Int) -> Unit
    ) {
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        // Create a request for DownloadManager
        val request = DownloadManager.Request(Uri.parse(documentUrl)).apply {
            setTitle("Descargando archivo")
            setDescription("Descargando $fileName")
            setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        }

        // Enqueue the request
        val downloadId = downloadManager.enqueue(request)

        // Create a BroadcastReceiver for download completion
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                if (id == downloadId) {
                    // Download completed
                    context.unregisterReceiver(this) // Unregister the receiver
                    openDownloadedFile(context, downloadId, downloadManager)
                    onProgress(100) // Progress complete
                }
            }
        }

        // Register the receiver
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE), Context.RECEIVER_NOT_EXPORTED)
        } else {
            context.registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        }

        // Poll for progress updates
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                delay(500) // Poll every 500ms
                val query = DownloadManager.Query().setFilterById(downloadId)
                val cursor = downloadManager.query(query)
                if (cursor != null && cursor.moveToFirst()) {
                    val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                    if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        // Download succeeded
                        break
                    } else if (status == DownloadManager.STATUS_FAILED) {
                        // Download failed
                        onProgress(-1) // Indicate failure
                        break
                    } else {
                        val totalBytes = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))
                        val downloadedBytes = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
                        if (totalBytes > 0) {
                            val progress = (downloadedBytes * 100) / totalBytes
                            onProgress(progress)
                        }
                    }
                }
                cursor?.close()
            }
        }
    }

    private fun openDownloadedFile(context: Context, downloadId: Long, downloadManager: DownloadManager) {
        val uri: Uri? = downloadManager.getUriForDownloadedFile(downloadId)
        if (uri != null) {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, context.contentResolver.getType(uri))
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(intent)
        } else {
            Toast.makeText(context, "No se pudo abrir el archivo", Toast.LENGTH_SHORT).show()
        }
    }

    suspend fun reviewActivity(body: ReviewEvaluationRequestDto){
        professorReviewsActivityUseCase(body).onEach { result ->
            when(result){
                is Resource.Error -> {
                    //Timber.tag("AUTH_VM").e("Error ${result.message?.uiMessage}")
                    Log.e("HOME_VM:", "Error ${result.message?.uiMessage}")
                    _stateReviewActivity.value = ReviewActivityState(error = result.message)
                }
                is Resource.Loading -> {
                    Timber.tag("HOME_VM").e("is loading")
                    _stateReviewActivity.value = ReviewActivityState(isLoading = true)
                }
                is Resource.Success -> {
                    Timber.tag("HOME_VM").e("success")
                    Log.e("HOME_VM:", "success")
                    _stateReviewActivity.value = ReviewActivityState(info = result.data)
                    Log.e("HOME_VM:", "${_stateReviewActivity.value.info}")
                    _stateReviewActivity.value.info?.let {
                        repositoryBundle.submissionsRepository.addOrUpdateSubmission(it)
//                        repositoryBundle.activitiesRepository.insertAllActivities(it)
//                        delay(1000)
                    }
                }
            }

        }.launchIn(viewModelScope)
    }




}