package com.example.classroom.presentation.screens.submission.composables

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.classroom.common.CustomButton.CustomButton
import com.example.classroom.common.CustomButton.NavigationButtonStyle
import com.example.classroom.common.FileUploadComponent.FileUploadComponent
import com.example.classroom.presentation.screens.submission.SubmissionViewModel

@Composable
fun SubmissionForm(
    viewModel: SubmissionViewModel,
    activityId: String,
    onSubmit: (Uri, String) -> Unit
) {
    var message by remember { mutableStateOf("") }
    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = "Enviar respuesta para la actividad", style = MaterialTheme.typography.h6)

        // File upload component
        FileUploadComponent(
            onFileSelected = { uri -> selectedFileUri = uri },
            onFileCleared = { selectedFileUri = null }
        )

        // Message input field
        OutlinedTextField(
            value = message,
            onValueChange = { message = it },
            label = { Text("Mensaje") },
            modifier = Modifier.fillMaxWidth()
        )

        // Submit button
        CustomButton(
            onClick = {
                if (selectedFileUri != null && message.isNotBlank()) {
                    onSubmit(selectedFileUri!!, message)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            disabled = selectedFileUri == null && message.isBlank(),
            style = NavigationButtonStyle.OutlineOnly,
            color1 = Color.Black,
            color2 = Color.Black,
            text = "Enviar"
        )
    }
}
