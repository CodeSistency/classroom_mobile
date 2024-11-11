package com.example.classroom.common.FileUploadComponent

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.classroom.common.CustomButton.CustomButton
import com.example.classroom.common.CustomButton.NavigationButtonStyle
import com.example.classroom.presentation.theme.Azul
import com.example.classroom.presentation.theme.AzulGradient

@Composable
fun FileUploadComponent(
    onFileSelected: (Uri) -> Unit,
    onFileCleared: () -> Unit
) {
    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    // ActivityResultLauncher to handle file selection
    val activityResultLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        selectedFileUri = uri
        if (uri != null) {
            onFileSelected(uri)
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Adjuntar archivo", style = MaterialTheme.typography.subtitle1)

        // File preview box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                .clickable {
                    activityResultLauncher.launch("*/*") // Opens file picker for all types
                },
            contentAlignment = Alignment.Center
        ) {
            if (selectedFileUri != null) {
                // Display preview or file name
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "Archivo seleccionado: ${selectedFileUri?.lastPathSegment}")

                }
            } else {
                Text(text = "Toca para seleccionar un archivo")
            }
        }

        // Clear file button
        if (selectedFileUri != null) {
            CustomButton(
                onClick = {
                    selectedFileUri = null
                    onFileCleared()
                },
                text = "Eliminar archivo",
                style = NavigationButtonStyle.SolidGradient,
                color1 = Azul,
                color2 = AzulGradient)
//            Button(
//                onClick = {
//                    selectedFileUri = null
//                    onFileCleared()
//                },
//                modifier = Modifier.padding(top = 8.dp)
//            ) {
//                Text("Eliminar archivo")
//            }
        }
    }
}
