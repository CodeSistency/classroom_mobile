package com.example.classroom.common.FileUploadComponent

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.InsertDriveFile
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
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

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "Adjuntar archivo",
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // File Preview Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(Color(0xFFEFEFEF), shape = RoundedCornerShape(12.dp))
                .clickable {
                    activityResultLauncher.launch("*/*") // Opens file picker for all types
                },
            contentAlignment = Alignment.Center
        ) {
            when {
                selectedFileUri == null -> {
                    Text(
                        text = "Selecciona un archivo",
                        style = MaterialTheme.typography.body1,
                        color = Color.Gray
                    )
                }
                else -> {
                    val fileType = getFileType(context, selectedFileUri!!)
                    when (fileType) {
                        "image" -> {
                            AsyncImage(
                                model = selectedFileUri,
                                contentDescription = "Selected Image",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(150.dp)
                                    .clip(RoundedCornerShape(12.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }
                        "pdf" -> {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PictureAsPdf,
                                    contentDescription = "PDF Preview",
                                    modifier = Modifier.size(40.dp),
                                    tint = MaterialTheme.colors.primary
                                )
                                Text(
                                    text = "PDF Archivo",
                                    style = MaterialTheme.typography.body1,
                                    color = MaterialTheme.colors.primary
                                )
                            }
                        }
                        else -> {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.InsertDriveFile,
                                    contentDescription = "File Preview",
                                    modifier = Modifier.size(40.dp),
                                    tint = MaterialTheme.colors.primary
                                )
                                Text(
                                    text = selectedFileUri?.lastPathSegment ?: "Archivo desconocido",
                                    style = MaterialTheme.typography.body2,
                                    color = MaterialTheme.colors.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }

        // Clear file button
        if (selectedFileUri != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    selectedFileUri = null
                    onFileCleared()
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Limpiar")
            }
        }
    }
}

fun getFileType(context: Context, uri: Uri): String {
    val contentResolver = context.contentResolver
    val mimeType = contentResolver.getType(uri)
    return when {
        mimeType?.startsWith("image") == true -> "image"
        mimeType == "application/pdf" -> "pdf"
        else -> "unknown"
    }
}
