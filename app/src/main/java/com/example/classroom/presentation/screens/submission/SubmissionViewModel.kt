package com.example.classroom.presentation.screens.submission

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classroom.common.uiState.UiState
import com.example.classroom.data.remote.dto.evaluations.reviewEvaluationDto.ReviewEvaluationRequestDto
import com.example.classroom.data.remote.dto.evaluations.sendEvaluationRequestDto.SendEvaluationRequestDto
import com.example.classroom.data.repository.RepositoryBundle
import com.example.classroom.domain.model.entity.LocalActivitySubmission
import com.example.classroom.domain.model.entity.Status
import com.example.classroom.domain.use_case.evaluations.professorReviewsActivityUseCase.ProfessorReviewsActivityUseCase
import com.example.classroom.domain.use_case.evaluations.studentSendActivityUseCase.StudentSendActivityUseCase
import com.example.classroom.presentation.screens.activity.studentEvaluations.states.StudentEvaluationsState
import com.example.classroom.presentation.screens.submission.states.ReviewActivityState
import com.example.classroom.presentation.screens.submission.states.SendActivityState
import io.ktor.client.statement.bodyAsText
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
    val studentSendActivityUseCase: StudentSendActivityUseCase
) : ViewModel() {

    private val _stateSendActivity = mutableStateOf(SendActivityState())
    val stateSendActivity: State<SendActivityState> = _stateSendActivity

    private val _stateReviewActivity = mutableStateOf(ReviewActivityState())
    val stateReviewActivity: State<ReviewActivityState> = _stateReviewActivity
    var currentSubmission: LocalActivitySubmission? = null
        private set

    // Load submission data for the professor to review
    fun loadSubmission(activityId: String, studentId: String) {
        viewModelScope.launch {
            currentSubmission = repositoryBundle.submissionsRepository
                .getSubmissionsForStudent(activityId, studentId)
                .first().first()
        }
    }

    // Update the grade locally
    fun updateGrade(newGrade: Float) {
        currentSubmission = currentSubmission?.copy(grade = newGrade)
    }

    // Submit the updated grade to the repository
    fun submitGrade() {
        currentSubmission?.let { submission ->
            viewModelScope.launch {
                repositoryBundle.submissionsRepository.addOrUpdateSubmission(submission)
            }
        }
    }

    fun submitStudentResponse(
        context: Context,
        activityId: String,
        fileUri: Uri,
        message: String,
        onSubmissionSuccess: () -> Unit,
        onSubmissionFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                // Convert URI to File and upload to server
                val file = getFileFromUri(context, fileUri)
                val uploadResponse = file?.let {
                    repositoryBundle.submissionsRepository.uploadFile(
                        it
                    )
                }

                if (uploadResponse != null) {
                    if (uploadResponse.status.value < 300) {
                        val fileUrl = uploadResponse.bodyAsText()
                        Log.e("fileurl", fileUrl.toString())
                        if (fileUrl != null) {

                            // Create submission in the local database
            //                        val submission = LocalActivitySubmission(
            //                            activityId = activityId,
            //                            studentId = "", // Replace with actual student ID retrieval
            //                            submissionDate = getCurrentDate(),
            //                            comment = message,
            //
            //                            documentUrl = fileUrl,
            //                            grade = 0f // Initial grade; professors will update this
            //                        )
            //                        repositoryBundle.submissionsRepository.addOrUpdateSubmission(submission)


                            val submission = SendEvaluationRequestDto(
                                userId = 0,
                                activityId = 0,
                                message = "",
                                document = "",

                            )
                            sendActivity(submission)
                            onSubmissionSuccess()
                        } else {
                            onSubmissionFailure("Error: Unable to retrieve uploaded file URL.")
                        }
                    } else {
                        onSubmissionFailure("Error: File upload failed.")
                    }
                }

//                onSubmissionFailure("Error")

            } catch (e: Exception) {
                e.printStackTrace()
                onSubmissionFailure("An error occurred: ${e.message}")
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

    fun downloadAndOpenFile(context: Context, documentUrl: String, fileName: String = "document") {
        viewModelScope.launch {
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

            // Register receiver to listen for download completion
            context.registerReceiver(object : BroadcastReceiver() {
                override fun onReceive(ctxt: Context, intent: Intent) {
                    val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                    if (id == downloadId) {
                        context.unregisterReceiver(this)
                        openDownloadedFile(context, downloadId, downloadManager)
                    }
                }
            }, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
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
//                        repositoryBundle.activitiesRepository.insertAllActivities(it)
//                        delay(1000)
                    }
                }
            }

        }.launchIn(viewModelScope)
    }

    suspend fun sendActivity(body: SendEvaluationRequestDto){
        studentSendActivityUseCase(body).onEach { result ->
            when(result){
                is Resource.Error -> {
                    //Timber.tag("AUTH_VM").e("Error ${result.message?.uiMessage}")
                    Log.e("HOME_VM:", "Error ${result.message?.uiMessage}")
                    _stateSendActivity.value = SendActivityState(error = result.message)
                }
                is Resource.Loading -> {
                    Timber.tag("HOME_VM").e("is loading")
                    _stateSendActivity.value = SendActivityState(isLoading = true)
                }
                is Resource.Success -> {
                    Timber.tag("HOME_VM").e("success")
                    Log.e("HOME_VM:", "success")
                    _stateSendActivity.value = SendActivityState(info = result.data)
                    Log.e("HOME_VM:", "${_stateSendActivity.value.info}")
                    _stateSendActivity.value.info?.let {
//                        repositoryBundle.activitiesRepository.insertAllActivities(it)
//                        delay(1000)
                    }
                }
            }

        }.launchIn(viewModelScope)
    }


}