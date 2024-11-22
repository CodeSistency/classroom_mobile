package com.example.classroom.presentation.screens.course.posts.addPost.composable

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.classroom.common.CustomButton.CustomButton
import com.example.classroom.common.CustomButton.NavigationButtonStyle
import com.example.classroom.common.CustomInput.CustomTextField
import com.example.classroom.common.composables.customDialogs.SetupCustomDialog
import com.example.classroom.common.composables.customDialogs.SetupCustomDialogState
import com.example.classroom.presentation.navigation.Destination
import com.example.classroom.presentation.screens.course.posts.addPost.AddPostViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AddPostForm(viewModel: AddPostViewModel, focusManager: FocusManager, courseId: String, idPost: String?, navController: NavController) {

    var state = viewModel.statePost.collectAsState()

    var dialogState: SetupCustomDialogState by remember {
        mutableStateOf(SetupCustomDialogState.Default())
    }

    LaunchedEffect(key1 = true, block = {
//        viewModel.f
    })

    var scope = rememberCoroutineScope()

    Column(modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)) {
        CustomTextField(
            value = viewModel.title.value,
            onValueChange = {
                viewModel.title.value = it
                viewModel.validateTitle()
            },
            label = "Title",
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
            label = "Content",
            errorMessage = viewModel.contentError.value ?: "",
            onNextClick = {
                focusManager.moveFocus(FocusDirection.Down)
            }
        )

//        CustomTextField(
//            value = viewModel.courseId.value.toString(),
//            onValueChange = {
//                viewModel.courseId.value = it.toIntOrNull() ?: 0
//                viewModel.validateCourseId()
//            },
//            label = "Course ID",
//            errorMessage = viewModel.courseIdError.value ?: "",
//            onNextClick = {
//                focusManager.moveFocus(FocusDirection.Down)
//            }
//        )
//
//        CustomTextField(
//            value = viewModel.authorId.value.toString(),
//            onValueChange = {
//                viewModel.authorId.value = it.toIntOrNull() ?: 0
//                viewModel.validateAuthorId()
//            },
//            label = "Author ID",
//            errorMessage = viewModel.authorIdError.value ?: "",
//            onNextClick = {
//                focusManager.moveFocus(FocusDirection.Down)
//            }
//        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomButton(
            text = "Publicar",
            style = NavigationButtonStyle.SolidGradient,
            color1 = Color(0xFF4CAF50),
            color2 = Color(0xFF81C784),
            onClick = {
                scope.launch {
                    viewModel.executeCourseRequest(idPost, courseId)
                }
            },
            disabled = !viewModel.isFormValid
        )
    }

    LaunchedEffect(key1 = state.value, block = {
        Log.e("POST STATE", state.value.toString())
        when{
            state.value.isLoading -> {
                dialogState = SetupCustomDialogState.Loading()
            }
            state.value.error != null -> {
                dialogState = SetupCustomDialogState.Error(state.value.error)
            }

            else -> {
                if (state.value.info != null){
                    dialogState = SetupCustomDialogState.Success(message = "Se ha creado la publicacion exitosamente exitosamente")
                    delay(1000)
                    navController.popBackStack()

                }
            }
        }
    })

    SetupCustomDialog(setupCustomDialogState = dialogState, showDialog = dialogState != SetupCustomDialogState.Default()) {
        dialogState = SetupCustomDialogState.Default()
    }
}