package com.example.classroom.presentation.screens.course.posts.addPost.composable

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Checkbox
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
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
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.classroom.R
import com.example.classroom.common.composables.CustomButton.CustomButton
import com.example.classroom.common.composables.CustomButton.NavigationButtonStyle
import com.example.classroom.common.composables.CustomInput.CustomTextField
import com.example.classroom.common.composables.FileUploadComponent.FileUploadComponent
import com.example.classroom.common.composables.FormWrapper.FormWrapper
import com.example.classroom.common.composables.customDialogs.SetupCustomDialog
import com.example.classroom.common.composables.customDialogs.SetupCustomDialogState
import com.example.classroom.common.composables.formScaffold.FormScaffold
import com.example.classroom.presentation.screens.course.posts.addPost.AddPostViewModel
import com.example.classroom.presentation.theme.Azul
import com.example.classroom.presentation.theme.AzulGradient
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AddPostForm(
    viewModel: AddPostViewModel,
    focusManager: FocusManager,
    courseId: String,
    idPost: String?,
    navController: NavController
) {
    val context = LocalContext.current
    val state = viewModel.statePost.collectAsState()
    var dialogState: SetupCustomDialogState by remember {
        mutableStateOf(SetupCustomDialogState.Default())
    }
    var isFileUploadChecked by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = state.value) {
        when {
            state.value.isLoading -> {
                dialogState = SetupCustomDialogState.Loading()
            }
            state.value.error != null -> {
                dialogState = SetupCustomDialogState.Error(state.value.error)
            }
            else -> {
                if (state.value.info != null) {
                    dialogState = SetupCustomDialogState.Success(message = "Se ha creado la publicación exitosamente")
                    delay(1000)
                    navController.popBackStack()
                    viewModel.cleanData()
                    viewModel.resetState()
                }
            }
        }
    }

    FormScaffold(
        title = "Crear publicación",
        onBackClick = { navController.popBackStack() },
        primaryButton = {
            CustomButton(
                text = "Publicar",
                style = NavigationButtonStyle.SolidGradient,
                color1 = Azul,
                color2 = AzulGradient,
                onClick = {
                    scope.launch {
                        viewModel.executeCourseRequest(idPost, courseId, isFileUploadChecked, context, false)
                    }
                },
                disabled = !viewModel.isFormValid
            )
        }
    ) {
        CustomTextField(
            value = viewModel.title.value,
            onValueChange = {
                viewModel.title.value = it
                viewModel.validateTitle()
            },
            label = "Titulo",
            errorMessage = viewModel.titleError.value ?: "",
            onNextClick = {
                focusManager.moveFocus(FocusDirection.Down)
            }
        )

        CustomTextField(
            value = viewModel.content.value,
            onValueChange = {
                viewModel.content.value = it
                viewModel.validateContent()
            },
            label = "Contenido",
            errorMessage = viewModel.contentError.value ?: "",
            onNextClick = {
                focusManager.moveFocus(FocusDirection.Down)
            }
        )

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Checkbox(
                checked = isFileUploadChecked,
                onCheckedChange = { isChecked ->
                    isFileUploadChecked = isChecked
                    if (!isChecked) {
                        viewModel.selectedFileUri = null
                    }
                }
            )
            Text(text = "Adjuntar archivo", style = MaterialTheme.typography.body1)
        }

        if (isFileUploadChecked) {
            FileUploadComponent(
                onFileSelected = { uri ->
                    viewModel.selectedFileUri = uri
                },
                onFileCleared = {
                    viewModel.selectedFileUri = null
                }
            )
        }
    }

    SetupCustomDialog(setupCustomDialogState = dialogState, showDialog = dialogState != SetupCustomDialogState.Default()) {
        dialogState = SetupCustomDialogState.Default()
    }
}
