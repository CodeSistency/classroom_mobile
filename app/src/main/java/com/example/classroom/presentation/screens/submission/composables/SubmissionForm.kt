package com.example.classroom.presentation.screens.submission.composables

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.classroom.common.composables.CustomButton.CustomButton
import com.example.classroom.common.composables.CustomButton.NavigationButtonStyle
import com.example.classroom.common.composables.CustomInput.CustomTextField
import com.example.classroom.common.composables.FileUploadComponent.FileUploadComponent
import com.example.classroom.presentation.screens.submission.SubmissionViewModel
import com.example.classroom.presentation.theme.Azul
import com.example.classroom.presentation.theme.AzulGradient

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SubmissionForm(
    viewModel: SubmissionViewModel,
    activityId: String,
    onSubmit: (Uri, String) -> Unit,
    navController: NavController
) {
    var message by remember { mutableStateOf("") }
    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }

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
                    Text(text = "Enviar evaluación", color = Color.White, fontSize = 16.sp)
                }
            }
        ){
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
//        OutlinedTextField(
//            value = message,
//            onValueChange = { message = it },
//            label = { Text("Mensaje") },
//            modifier = Modifier.fillMaxWidth()
//        )

                CustomTextField(value = message,
                    onValueChange = { message = it },
                    label = "Mensaje",
                    modifier = Modifier.fillMaxWidth(),
                    onNextClick = {})

                // Submit button
                CustomButton(
                    onClick = {
                        if (selectedFileUri != null && message.isNotBlank()) {
                            onSubmit(selectedFileUri!!, message)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    disabled = selectedFileUri == null && message.isBlank(),
                    style = NavigationButtonStyle.SolidGradient,
                    color1 = Azul,
                    color2 = AzulGradient,
                    text = "Enviar"
                )
            }

        }
    }
}
