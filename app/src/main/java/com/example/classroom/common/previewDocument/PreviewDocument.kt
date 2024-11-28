package com.example.classroom.common.previewDocument

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Download
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
import coil.compose.AsyncImagePainter
import com.example.classroom.common.getSupabaseFileUrl

@Composable
fun DocumentPreviewComponent(
    documentUrl: String,
    fileType: String, // e.g., "image", "pdf", "unknown"
    onDownloadFile: (String) -> Unit,
    modifier: Modifier = Modifier,
    isDownloading: Boolean = false // Flag to show downloading progress
) {
    val loadingState = remember { mutableStateOf(false) }
    val errorState = remember { mutableStateOf(false) }

    // Supabase media URL
    val mediaUrl = getSupabaseFileUrl(documentUrl)
    Log.d("DocumentPreview", "Media URL: $mediaUrl")
    Log.d("DocumentPreview", "FileType: $fileType")

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(12.dp))
            .padding(8.dp)
    ) {
        when {
            errorState.value -> {
                // Error state with retry
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Error,
                        contentDescription = "Error cargando archivo",
                        modifier = Modifier.size(40.dp),
                        tint = Color.Red
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Error al cargar archivo",
                        color = Color.Red,
                        style = MaterialTheme.typography.body2
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = {
                        loadingState.value = true
                        errorState.value = false
                    }) {
                        Text("Reintentar")
                    }
                }
            }

            else -> {
                when (fileType) {
                    "image" -> {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            // Image preview with loading state
                            if (loadingState.value) {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .size(40.dp),
                                    color = MaterialTheme.colors.primary
                                )
                            }
                            AsyncImage(
                                model = mediaUrl,
                                contentDescription = "Imagen preview",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(150.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop,
                                onLoading = { loadingState.value = true },
                                onSuccess = { loadingState.value = false },
                                onError = {
                                    errorState.value = true
                                    loadingState.value = false
                                }
                            )
                            // Download icon in the top-right corner
                            IconButton(
                                onClick = { onDownloadFile(mediaUrl) },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .size(24.dp)
                                    .background(Color.White, shape = CircleShape)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Download,
                                    contentDescription = "Descargar imagen",
                                    tint = MaterialTheme.colors.primary
                                )
                            }
                        }
                    }

                    "pdf" -> {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.PictureAsPdf,
                                contentDescription = "PDF Archivo",
                                modifier = Modifier.size(40.dp),
                                tint = MaterialTheme.colors.primary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "PDF Documento",
                                style = MaterialTheme.typography.body1,
                                color = MaterialTheme.colors.primary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            if (isDownloading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(40.dp),
                                    color = MaterialTheme.colors.primary
                                )
                            } else {
                                Button(onClick = { onDownloadFile(mediaUrl) }) {
                                    Text("Descargar PDF")
                                }
                            }
                        }
                    }

                    else -> {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.InsertDriveFile,
                                contentDescription = "Documento desconocido",
                                modifier = Modifier.size(40.dp),
                                tint = MaterialTheme.colors.primary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Archivo desconocido",
                                style = MaterialTheme.typography.body1,
                                color = MaterialTheme.colors.primary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            if (isDownloading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(40.dp),
                                    color = MaterialTheme.colors.primary
                                )
                            } else {
                                Button(onClick = { onDownloadFile(mediaUrl) }) {
                                    Text("Descargar")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
