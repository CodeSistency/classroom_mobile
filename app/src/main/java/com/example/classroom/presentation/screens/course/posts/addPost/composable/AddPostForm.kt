package com.example.classroom.presentation.screens.course.posts.addPost.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.classroom.common.CustomButton.CustomButton
import com.example.classroom.common.CustomButton.NavigationButtonStyle
import com.example.classroom.common.CustomInput.CustomTextField
import com.example.classroom.presentation.screens.course.posts.addPost.AddPostViewModel

@Composable
fun AddPostForm(viewModel: AddPostViewModel, focusManager: FocusManager, courseId: String, onSubmit: () -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
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

        CustomTextField(
            value = viewModel.courseId.value.toString(),
            onValueChange = {
                viewModel.courseId.value = it.toIntOrNull() ?: 0
                viewModel.validateCourseId()
            },
            label = "Course ID",
            errorMessage = viewModel.courseIdError.value ?: "",
            onNextClick = {
                focusManager.moveFocus(FocusDirection.Down)
            }
        )

        CustomTextField(
            value = viewModel.authorId.value.toString(),
            onValueChange = {
                viewModel.authorId.value = it.toIntOrNull() ?: 0
                viewModel.validateAuthorId()
            },
            label = "Author ID",
            errorMessage = viewModel.authorIdError.value ?: "",
            onNextClick = {
                focusManager.moveFocus(FocusDirection.Down)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomButton(
            text = "Publicar",
            style = NavigationButtonStyle.SolidGradient,
            color1 = Color(0xFF4CAF50),
            color2 = Color(0xFF81C784),
            onClick = onSubmit,
            disabled = !viewModel.isFormValid
        )
    }
}