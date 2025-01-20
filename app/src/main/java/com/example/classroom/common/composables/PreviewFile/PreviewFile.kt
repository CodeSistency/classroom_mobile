package com.example.classroom.common.composables.PreviewFile

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import proyecto.person.appconsultapopular.common.Constants

@Composable
fun FilePreview(
    fileUrl: String,
    fileName: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Detect if the file is an image
    val extension = fileUrl.substringAfterLast('.', "").lowercase()
    val mimeType = getMimeTypeFromExtension(extension)
    val isImageFile = mimeType.startsWith("image/")

    // Download states (for saving the file)
    var downloadStatus by remember { mutableStateOf(DownloadStatus.Idle) }
    var downloadId by remember { mutableStateOf<Long?>(null) }

    // Fullscreen image states (after the file is downloaded, if you want to open it)
    var showFullScreenImage by remember { mutableStateOf(false) }
    var fullImageUri by remember { mutableStateOf<Uri?>(null) }

    var url = fileUrl.replace("localhost", Constants.HOST)

    // BroadcastReceiver to check success/failure of the DownloadManager
    DisposableEffect(Unit) {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val action = intent.action
                val completedId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

                Log.e("FilePreview", "onReceive called. action=$action, completedId=$completedId, our downloadId=$downloadId")

                if (action == DownloadManager.ACTION_DOWNLOAD_COMPLETE && completedId == downloadId) {
                    // Check the final status
                    val (status, reason) = getDownloadStatusAndReason(context, completedId)
                    Log.e("FilePreview", "DownloadManager result -> status=$status, reason=$reason")

                    if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        // For an image, optionally show fullscreen
                        if (isImageFile) {
                            val localUri = getLocalUriFromDownloadId(context, completedId)
                            Log.e("FilePreview", "Downloaded image localUri=$localUri")
                            if (localUri != null) {
                                fullImageUri = localUri
                                showFullScreenImage = true
                            }
                        } else {
                            // If not image, open with an external app
                            val localUri = getLocalUriFromDownloadId(context, completedId)
                            Log.e("FilePreview", "Downloaded file localUri=$localUri")
                            if (localUri != null) {
                                openFileWithIntent(context, localUri, mimeType)
                            }
                        }
                        downloadStatus = DownloadStatus.Completed
                    } else {
                        // Show that the download failed
                        downloadStatus = DownloadStatus.Error
                        Log.e("FilePreview", "Download failed with reason=$reason")
                    }
                    // Reset the ID so we don't process it again
                    downloadId = null
                }
            }
        }

        val filter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.registerReceiver(receiver, filter, Context.RECEIVER_EXPORTED)
        } else {
            context.registerReceiver(receiver, filter)
        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            Log.e("FilePreview", "Registering broadcast receiver with RECEIVER_NOT_EXPORTED")
//            context.registerReceiver(receiver, filter, Context.RECEIVER_NOT_EXPORTED)
//        } else {
//            Log.e("FilePreview", "Registering broadcast receiver normally")
//            context.registerReceiver(receiver, filter)
//        }

        onDispose {
            Log.e("FilePreview", "Unregistering broadcast receiver")
            context.unregisterReceiver(receiver)
        }
    }
    // The UI Card/Box
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color(0xFFF9F9F9), shape = RoundedCornerShape(8.dp))
            .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        if (isImageFile) {
            Column {
                Text(
                    text = fileName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF333333)
                )
                Spacer(Modifier.height(8.dp))

                // Here’s our new subcomposable
                ImagePreviewWithRetry(
                    url = url,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        // Show the same remote URL in fullscreen
                        fullImageUri = Uri.parse(url)
                        showFullScreenImage = true
                    }
                )

                // Download status indicators (if you want to display them)
                if (downloadStatus == DownloadStatus.Downloading) {
                    Spacer(Modifier.height(8.dp))
                    LinearProgressIndicator(
                        color = Color(0xFF4B6BEF),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text("Downloading...", color = Color.Gray, fontSize = 13.sp)
                } else if (downloadStatus == DownloadStatus.Completed) {
                    Spacer(Modifier.height(4.dp))
                    Text("Download Complete!", color = Color(0xFF388E3C))
                } else if (downloadStatus == DownloadStatus.Error) {
                    Spacer(Modifier.height(4.dp))
                    Text("Download failed!", color = Color.Red)
                }

                // Possibly a "download icon" in bottom-right corner?
                // For example:
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = "Download image",
                        tint = Color(0xFF4B6BEF),
                        modifier = Modifier
                            .size(28.dp)
                            .clickable {
                                if (downloadStatus != DownloadStatus.Downloading) {
                                    downloadStatus = DownloadStatus.Downloading
                                    downloadId = enqueueDownload(
                                        context,
                                        url,
                                        fileName,
                                        mimeType
                                    )
                                }
                            }
                    )
                }
            }
        } else {
            // Non-image file preview
            Column {
                Text(
                    text = fileName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF333333)
                )
                Spacer(Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Description,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(Modifier.width(16.dp))
                    Text(
                        text = extension.uppercase(),
                        color = Color.DarkGray,
                        fontSize = 15.sp
                    )

                    Spacer(Modifier.weight(1f))
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = "Download doc",
                        tint = Color(0xFF4B6BEF),
                        modifier = Modifier
                            .size(28.dp)
                            .clickable {
                                if (downloadStatus != DownloadStatus.Downloading) {
                                    downloadStatus = DownloadStatus.Downloading
                                    downloadId = enqueueDownload(
                                        context,
                                        fileUrl,
                                        fileName,
                                        mimeType
                                    )
                                }
                            }
                    )
                }

                // Show progress or error for doc files
                if (downloadStatus == DownloadStatus.Downloading) {
                    Spacer(Modifier.height(8.dp))
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFF4B6BEF)
                    )
                    Text("Downloading...", color = Color.Gray, fontSize = 13.sp)
                } else if (downloadStatus == DownloadStatus.Completed) {
                    Spacer(Modifier.height(4.dp))
                    Text("Download Complete!", color = Color(0xFF388E3C))
                } else if (downloadStatus == DownloadStatus.Error) {
                    Spacer(Modifier.height(4.dp))
                    Text("Download failed!", color = Color.Red)
                }
            }
        }
    }

    // Show fullscreen image if desired (after DownloadManager finishes)
    if (showFullScreenImage && fullImageUri != null) {
        Dialog(onDismissRequest = {
            showFullScreenImage = false
            fullImageUri = null
        }) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.7f))
                    .clickable {
                        showFullScreenImage = false
                        fullImageUri = null
                    },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = rememberAsyncImagePainter(fullImageUri),
                    contentDescription = "Fullscreen image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}
// Simple enum for status
enum class DownloadStatus {
    Idle,
    Downloading,
    Completed,
    Error
}


// Colors
private val CardBackgroundColor = Color(0xFFF9F9F9)
private val CardBorderColor = Color(0xFFE0E0E0)
private val TextColorPrimary = Color(0xFF3A3A3A)
private val TextColorSecondary = Color(0xFF6C6C6C)
private val ErrorColor = Color(0xFFD32F2F)
private val SuccessColor = Color(0xFF388E3C)
private val OverlayColor = Color(0x80000000) // semi-transparent black
private val ButtonColor = Color(0xFF4B6BEF)
private val ButtonTextColor = Color.White

// Shapes / corner radii
private val CardCornerShape = RoundedCornerShape(12.dp)

// Simple text style (title, body, etc.)
@Composable
private fun TitleText(text: String) {
    Text(
        text = text,
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        color = TextColorPrimary
    )
}

@Composable
private fun BodyText(text: String, color: Color = TextColorSecondary, fontSize: TextUnit = 14.sp) {
    Text(
        text = text,
        color = color,
        fontSize = fontSize
    )
}

@Composable
fun ImagePreviewWithRetry(
    url: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}

) {
    // We keep a "refreshKey" in state so we can force Coil to re-fetch
    var refreshKey by remember { mutableStateOf(0) }

    // Build an ImageRequest with the URL. We append `refreshKey` as a query param
    // so Coil sees a different URL each time we "retry".
    val context = LocalContext.current
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data("$url?retry=$refreshKey")
            .build()
    )

    val painterState = painter.state

    Box(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 150.dp, max = 300.dp)
            .clickable { onClick() }  // <--- ADD clickable

    ) {
        // Show the image (if success or still loading)
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // LOADING OVERLAY
        if (painterState is AsyncImagePainter.State.Loading) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color.Black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        }

        // ERROR OVERLAY with RETRY button
        if (painterState is AsyncImagePainter.State.Error) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color.Red.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Error loading image!",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { refreshKey++ },
                        colors = ButtonDefaults.buttonColors(

                            backgroundColor = Color.White
                        )
                    ) {
                        Text(
                            text = "Retry",
                            color = Color.Red,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
