package com.example.classroom.common.previewDocument

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.InsertDriveFile
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun DocumentPreviewComponent(
    documentUrl: String,
    fileType: String, // e.g., "image", "pdf", "unknown"
    onDownloadFile: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val loadingState = remember { mutableStateOf(true) }
    val errorState = remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(12.dp))
            .clickable { if (!loadingState.value && !errorState.value) onDownloadFile(documentUrl) }
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        if (loadingState.value) {
            CircularProgressIndicator(
                modifier = Modifier.size(40.dp),
                color = MaterialTheme.colors.primary
            )
        } else if (errorState.value) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Error,
                    contentDescription = "Error loading file",
                    modifier = Modifier.size(40.dp),
                    tint = Color.Red
                )
                Text(
                    text = "Error loading file",
                    color = Color.Red,
                    style = MaterialTheme.typography.body2
                )
            }
        } else {
            when (fileType) {
                "image" -> {
                    AsyncImage(
                        model = documentUrl,
                        contentDescription = "Image preview",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop,
                        onLoading = { loadingState.value = true },
                        onError = {
                            errorState.value = true
                            loadingState.value = false
                        },
                        onSuccess = { loadingState.value = false }
                    )
                }
                "pdf" -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.PictureAsPdf,
                            contentDescription = "PDF File",
                            modifier = Modifier.size(40.dp),
                            tint = MaterialTheme.colors.primary
                        )
                        Text(
                            text = "PDF Document",
                            style = MaterialTheme.typography.body1,
                            color = MaterialTheme.colors.primary
                        )
                    }
                }
                else -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.InsertDriveFile,
                            contentDescription = "Unknown File",
                            modifier = Modifier.size(40.dp),
                            tint = MaterialTheme.colors.primary
                        )
                        Text(
                            text = "Download File",
                            style = MaterialTheme.typography.body1,
                            color = MaterialTheme.colors.primary
                        )
                    }
                }
            }
        }
    }
}
