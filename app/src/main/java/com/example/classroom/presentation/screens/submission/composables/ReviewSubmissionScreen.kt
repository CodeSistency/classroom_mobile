package com.example.classroom.presentation.screens.submission.composables

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.classroom.common.composables.CustomButton.CustomButton
import com.example.classroom.common.composables.CustomButton.NavigationButtonStyle
import com.example.classroom.common.composables.CustomInput.CustomTextField
import com.example.classroom.common.composables.FormWrapper.FormWrapper
import com.example.classroom.common.composables.PreviewFile.FilePreview
import com.example.classroom.common.composables.customDialogs.SetupCustomDialog
import com.example.classroom.common.composables.customDialogs.SetupCustomDialogState
import com.example.classroom.common.composables.previewDocument.DocumentPreviewComponent
import com.example.classroom.common.getSupabaseFileUrl
import com.example.classroom.data.remote.dto.evaluations.reviewEvaluationDto.ReviewEvaluationRequestDto
import com.example.classroom.domain.model.entity.LocalActivitySubmission
import com.example.classroom.presentation.navigation.Destination
import com.example.classroom.presentation.screens.submission.SubmissionViewModel
import com.example.classroom.presentation.theme.Azul
import com.example.classroom.presentation.theme.Azul2
import com.example.classroom.presentation.theme.AzulGradient
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ReviewSubmissionScreen(
    submission: LocalActivitySubmission,

    viewModel: SubmissionViewModel,
    navController: NavController
) {
    val state = viewModel.stateReviewActivity.collectAsState()

var context = LocalContext.current
    LaunchedEffect(key1 = true, block = {
        viewModel.grade.value = submission.grade
    })



    var dialogState: SetupCustomDialogState by remember {
        mutableStateOf(SetupCustomDialogState.Default())
    }
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()){
        Scaffold(
            topBar = {
                Row(
                    modifier= Modifier
                        .background(Azul)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null,
                            tint = Color.White)
                    }
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(text = "Evaluar", color = Color.White, fontSize = 16.sp)
                }
            }
        ){
            FormWrapper {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Document preview and download
                    if (submission.documentUrl != null) {
                        var downloadProgress by remember { mutableStateOf(0) }

//                    DocumentPreviewComponent(
//                        documentUrl = submission.documentUrl,
//                        fileType = getFileType(submission.documentUrl),
//                        onDownloadFile = { url ->
//                            viewModel.downloadAndOpenFile(context, url, "file_name.ext") { progress ->
//                                downloadProgress = progress
//                            }
//                        },
//                        isDownloading = downloadProgress in 1..99 // Show progress indicator
//                    )

                        Log.e("submission document", submission.documentUrl)

                        val mediaUrl = getSupabaseFileUrl(submission.documentUrl, isPublic = true, useSupabase = false)

                        Log.e("mediaurl", mediaUrl)
                        FilePreview(
                            mediaUrl,
                            fileName = ""
                        )
                    } else {
                        Text(text = "Ningun documento.", style = MaterialTheme.typography.body2)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Display student's comment
                    Text(text = "Comentario del estudiante:", style = MaterialTheme.typography.subtitle1)
                    submission.comment?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.body2,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                                .padding(8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Grade input
//        Text(text = "Calificación (0-100):", style = MaterialTheme.typography.subtitle1)
//        OutlinedTextField(
//            value = grade.toString(),
//            onValueChange = { value ->
//                val newGrade = value.toFloatOrNull()
//                if (newGrade != null && newGrade in 0f..100f) {
//                    grade = newGrade
//                    onGradeChange(newGrade)
//                }
//            },
//            modifier = Modifier.fillMaxWidth(),
//            singleLine = true,
//            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
//        )


                    CustomTextField(
                        value = viewModel.grade.value.takeIf { it in 0.0..100.0 }?.toString() ?: "",
                        onValueChange = { value ->
                            val sanitizedValue = value.filter { it.isDigit() || it == '.' } // Allow only digits and dot
                            val newGrade = sanitizedValue.toDoubleOrNull()

                            if (newGrade != null && newGrade in 0.0..100.0) {
                                viewModel.grade.value = newGrade // Update grade if within range
                            } else if (value.isEmpty()) {
                                viewModel.grade.value = 0.0 // Default to 0 if input is empty
                            }
                        },
                        label = "Calificación (0-100)",
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        onNextClick = {
                            // Handle done or next action
                            Log.d("CustomTextField", "Grade input completed: ${viewModel.grade.value}")
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Submit grade button
                    CustomButton(
                        onClick = {
                            scope.launch {
                                viewModel.reviewActivity(
                                    body = ReviewEvaluationRequestDto(
                                        activityId = submission.activityId.toInt(),
                                        grade = viewModel.grade.value.toInt(),
                                    )
                                )
                            }
                        },
                        disabled = viewModel.grade.value < 1,
                        modifier = Modifier.fillMaxWidth(),
                        text = "Guardar calificacion",
                        style = NavigationButtonStyle.SolidGradient,
                        color2 = Azul,
                        color1 = AzulGradient

                    )
                }

            }
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp),
//                verticalArrangement = Arrangement.spacedBy(8.dp)
//            ) {
//                // Document preview and download
//                if (submission.documentUrl != null) {
//                    var downloadProgress by remember { mutableStateOf(0) }
//
////                    DocumentPreviewComponent(
////                        documentUrl = submission.documentUrl,
////                        fileType = getFileType(submission.documentUrl),
////                        onDownloadFile = { url ->
////                            viewModel.downloadAndOpenFile(context, url, "file_name.ext") { progress ->
////                                downloadProgress = progress
////                            }
////                        },
////                        isDownloading = downloadProgress in 1..99 // Show progress indicator
////                    )
//                    FilePreview(
//                        submission.documentUrl,
//                        fileName = ""
//                    )
//                } else {
//                    Text(text = "Ningun documento.", style = MaterialTheme.typography.body2)
//                }
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                // Display student's comment
//                Text(text = "Comentario del estudiante:", style = MaterialTheme.typography.subtitle1)
//                submission.comment?.let {
//                    Text(
//                        text = it,
//                        style = MaterialTheme.typography.body2,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
//                            .padding(8.dp)
//                    )
//                }
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                // Grade input
////        Text(text = "Calificación (0-100):", style = MaterialTheme.typography.subtitle1)
////        OutlinedTextField(
////            value = grade.toString(),
////            onValueChange = { value ->
////                val newGrade = value.toFloatOrNull()
////                if (newGrade != null && newGrade in 0f..100f) {
////                    grade = newGrade
////                    onGradeChange(newGrade)
////                }
////            },
////            modifier = Modifier.fillMaxWidth(),
////            singleLine = true,
////            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
////        )
//
//
//                CustomTextField(
//                    value = viewModel.grade.value.takeIf { it in 0.0..100.0 }?.toString() ?: "",
//                    onValueChange = { value ->
//                        val sanitizedValue = value.filter { it.isDigit() || it == '.' } // Allow only digits and dot
//                        val newGrade = sanitizedValue.toDoubleOrNull()
//
//                        if (newGrade != null && newGrade in 0.0..100.0) {
//                            viewModel.grade.value = newGrade // Update grade if within range
//                        } else if (value.isEmpty()) {
//                            viewModel.grade.value = 0.0 // Default to 0 if input is empty
//                        }
//                    },
//                    label = "Calificación (0-100)",
//                    keyboardOptions = KeyboardOptions.Default.copy(
//                        keyboardType = KeyboardType.Number,
//                        imeAction = ImeAction.Done
//                    ),
//                    onNextClick = {
//                        // Handle done or next action
//                        Log.d("CustomTextField", "Grade input completed: ${viewModel.grade.value}")
//                    }
//                )
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                // Submit grade button
//                CustomButton(
//                    onClick = {
//                        scope.launch {
//                            viewModel.reviewActivity(
//                                body = ReviewEvaluationRequestDto(
//                                    activityId = submission.activityId.toInt(),
//                                    grade = viewModel.grade.value.toInt(),
//                                )
//                            )
//                        }
//                    },
//                    modifier = Modifier.fillMaxWidth(),
//                    text = "Guardar calificacion",
//                    style = NavigationButtonStyle.SolidGradient,
//                    color2 = Azul,
//                    color1 = AzulGradient
//
//                )
//            }

        }
    }

    LaunchedEffect(key1 = state.value, block = {
        when{
            state.value.isLoading -> {
                dialogState = SetupCustomDialogState.Loading()
                viewModel.cleanData()
            }
            state.value.error != null -> {
                dialogState = SetupCustomDialogState.Error(state.value.error!!.uiMessage)
                viewModel.cleanData()
            }

            else -> {
                if (state.value.info != null){
                    dialogState = SetupCustomDialogState.Success(message = "Se ha calificado la evaluacion exitosamente")
                    delay(1000)
                    navController.popBackStack()
                    viewModel.cleanData()
                }
            }
        }
    })

    SetupCustomDialog(setupCustomDialogState = dialogState, showDialog = dialogState != SetupCustomDialogState.Default()) {
        dialogState = SetupCustomDialogState.Default()
    }
}

fun getFileType(documentUrl: String): String {
    val sanitizedUrl = documentUrl.substringBefore('?') // Remove query parameters
    val fileExtension = sanitizedUrl.substringAfterLast('.', "").lowercase()
    Log.d("getFileType", "Sanitized URL: $sanitizedUrl, Extracted extension: $fileExtension")
    return when (fileExtension) {
        "jpg", "jpeg", "png", "gif", "bmp", "webp" -> "image"
        "pdf" -> "pdf"
        "doc", "docx" -> "word"
        "xls", "xlsx" -> "excel"
        "ppt", "pptx" -> "powerpoint"
        "txt" -> "text"
        "mp4", "avi", "mov", "mkv" -> "video"
        "mp3", "wav", "aac" -> "audio"
        else -> {
            Log.w("getFileType", "Unknown file type for URL: $documentUrl")
            "unknown"
        }
    }
}
