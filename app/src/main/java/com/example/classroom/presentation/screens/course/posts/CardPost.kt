package com.example.classroom.presentation.screens.course.posts

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.classroom.R
import com.example.classroom.common.CustomDialog
import com.example.classroom.common.composables.PreviewFile.FilePreview
import com.example.classroom.common.composables.cardWrapper.CardWrapper
import com.example.classroom.common.getSupabaseFileUrl
import com.example.classroom.domain.model.entity.LocalPost
import com.example.classroom.presentation.theme.PaddingCustom
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

//@Composable
//fun CardPostItem(post: LocalPost, viewModel: PostsViewModel, scope: CoroutineScope) {
//    val shape = RoundedCornerShape(PaddingCustom.MEDIUM.size)
//    var isDeleteOpen by remember { mutableStateOf(false) }
//    Box(modifier = Modifier) {
//        Box(
//            modifier = Modifier
//                .shadow(8.dp, shape)
//                .background(Color.White, shape)
//                .padding(16.dp)
//        ) {
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                Column {
//                    Text(
//                        text = post.title,
//                        style = TextStyle(
//                            color = Color.DarkGray,
//                            fontSize = 20.sp,
//                            fontWeight = FontWeight.Bold,
//                        )
//                    )
//                    Spacer(modifier = Modifier.height(5.dp))
//                    Text(
//                        text = post.content,
//                        style = TextStyle(
//                            color = Color.DarkGray,
//                            fontSize = 14.sp,
//                            fontWeight = FontWeight.Normal,
//                        )
//                    )
//                    Spacer(modifier = Modifier.height(5.dp))
//                    Text(
//                        text = "Created at: ${post.createdAt}",
//                        style = TextStyle(
//                            color = Color.Gray,
//                            fontSize = 10.sp,
//                            fontWeight = FontWeight.Light,
//                        )
//                    )
//                }
//                IconButton(onClick = {
//                    isDeleteOpen = true
//                }) {
//                    Icon(
//                        painterResource(id = R.drawable.ic_cancel),
//                        contentDescription = null,
//                        tint = Color.Gray,
//                        modifier = Modifier.size(35.dp)
//                    )
//                }
//            }
//        }
//        Box(
//            modifier = Modifier
//                .height(80.dp)
//                .width(5.dp)
//                .background(Color(0xFF4CAF50), RoundedCornerShape(PaddingCustom.MEDIUM.size))
//                .align(Alignment.CenterStart),
//        )
//    }
//
//    if (isDeleteOpen) {
//        CustomDialog(
//            message = "Are you sure you want to delete this post?",
//            messageBtn = "Delete",
//            loading = false,
//            action = { scope.launch{
//                viewModel.deletePostCourseRemote(post.idApi)
//            }  },
//            dismissDialog = { isDeleteOpen = false },
//            icon = painterResource(id = R.drawable.ic_person_remove)
//        )
//    }
//}

@Composable
fun CardPostItem(post: LocalPost, viewModel: PostsViewModel, scope: CoroutineScope, context: Context) {
    var isDeleteOpen by remember { mutableStateOf(false) }
    val userInfo by viewModel.userInfo.collectAsState()

    CardWrapper(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header with author and creation date
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = post.title,
                        style = TextStyle(
                            color = Color.DarkGray,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).apply {
                        timeZone = TimeZone.getTimeZone("UTC")
                    }
                    val outputFormat = SimpleDateFormat("yyyy/MM/dd", Locale.US)

                    val formattedDate = try {
                        val date = inputFormat.parse(post.createdAt)
                        outputFormat.format(date ?: Date())
                    } catch (e: Exception) {
                        "2000/01/01"
                    }

                    Text(
                        text = formattedDate,
                        style = TextStyle(
                            color = Color.Gray,
                            fontSize = 12.sp
                        )
                    )
                }

                userInfo?.let {
                    if (it.idApi == post.authorId) {
                        IconButton(onClick = { isDeleteOpen = true }) {
                            Icon(
                                painterResource(id = R.drawable.ic_cancel),
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }

            if (post.content.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = post.content,
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 14.sp
                    )
                )
            }

            // Media preview
            if (!post.mediaUrl.isNullOrEmpty()) {
                val mediaUrl = getSupabaseFileUrl(post.mediaUrl, isPublic = true, useSupabase = false)
                Spacer(modifier = Modifier.height(8.dp))
                FilePreview(fileUrl = mediaUrl, fileName = "")
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }

    // Delete confirmation dialog
    if (isDeleteOpen) {
        CustomDialog(
            message = "¿Estás seguro de que quieres eliminar esta publicación?",
            messageBtn = "Borrar",
            loading = false,
            action = { scope.launch { viewModel.deletePostCourseRemote(post.idApi) } },
            dismissDialog = { isDeleteOpen = false },
            icon = painterResource(id = R.drawable.ic_cancel)
        )
    }
}

//@Composable
//fun CardPostItem(post: LocalPost, viewModel: PostsViewModel, scope: CoroutineScope, context: Context) {
//
//    var isDeleteOpen by remember { mutableStateOf(false) }
//    val shape = RoundedCornerShape(12.dp)
//
//    val userInfo by viewModel.userInfo.collectAsState()
//
//    Box(
//        modifier = Modifier
//            .padding(8.dp)
//            .shadow(4.dp, shape)
//            .background(Color.White, shape)
//            .fillMaxWidth()
//    ) {
//        Column(
//            modifier = Modifier
//                .padding(16.dp)
//        ) {
//            // Header with author and creation date
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Column {
//                    Text(
//                        text = post.title,
//                        style = TextStyle(
//                            color = Color.DarkGray,
//                            fontSize = 16.sp,
//                            fontWeight = FontWeight.Bold
//                        )
//                    )
//
//                    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).apply {
//                        timeZone = TimeZone.getTimeZone("UTC") // Parse in UTC
//                    }
//
//                    val outputFormat = SimpleDateFormat("yyyy/MM/dd", Locale.US) // Desired output format
//
//                    val formattedDate = try {
//                        val date = inputFormat.parse(post.createdAt)
//                        outputFormat.format(date ?: Date()) // Format the date properly
//                    } catch (e: Exception) {
//                        "2000/01/01" // Default fallback in case of error
//                    }
//
//                    Text(
//                        text = "$formattedDate",
//                        style = TextStyle(
//                            color = Color.Gray,
//                            fontSize = 12.sp
//                        )
//                    )
//                }
//
//                userInfo?.let {
//
//                    if (it.idApi == post.authorId){
//                        IconButton(onClick = { isDeleteOpen = true }) {
//                            Icon(
//                                painterResource(id = R.drawable.ic_cancel),
//                                contentDescription = null,
//                                tint = Color.Gray,
//                                modifier = Modifier.size(24.dp)
//                            )
//                        }
//                    }
//                }
//
//            }
//
//            if (post.content.isNotBlank()){
//                Spacer(modifier = Modifier.height(8.dp))
//
//                // Content
//
//                Text(
//                    text = post.content,
//                    style = TextStyle(
//                        color = Color.Black,
//                        fontSize = 14.sp
//                    )
//                )
//            }
//
//
//
//            // Media preview
//            if (!post.mediaUrl.isNullOrEmpty()){
//                post.mediaUrl.let { url ->
//                    Log.e("mediaurl1", url)
//
//                    val mediaUrl = getSupabaseFileUrl(url, isPublic = true, useSupabase = false)
//
//                    Log.e("mediaurl2", mediaUrl)
//                    Spacer(modifier = Modifier.height(8.dp))
//
//                    FilePreview(
//                        fileUrl = mediaUrl,
//                        fileName = "",
////                        modifier = Modifier.padding(16.dp)
//                    )
//
////                    when {
////                        mediaUrl.endsWith(".jpg") || mediaUrl.endsWith(".jpeg") || mediaUrl.endsWith(".png") -> {
////                            // Image preview using AsyncImage
////                            AsyncImage(
////                                model = mediaUrl,
////                                contentDescription = "Post media image",
////                                modifier = Modifier
////                                    .fillMaxWidth()
////                                    .height(200.dp)
////                                    .clip(shape),
////                                contentScale = ContentScale.Crop
////                            )
////                        }
////                        else -> {
////                            // File preview
////                            FilePreviewCard(url = mediaUrl, context = context, viewModel = viewModel)
////                        }
////                    }
//                    Spacer(modifier = Modifier.height(8.dp))
//                }
//
//            }
//
//
//        }
//    }
//
//    // Delete confirmation dialog
//    if (isDeleteOpen) {
//        CustomDialog(
//            message = "¿Estás seguro de que quieres eliminar esta publicación?",
//            messageBtn = "Borrar",
//            loading = false,
//            action = { scope.launch { viewModel.deletePostCourseRemote(post.idApi) } },
//            dismissDialog = { isDeleteOpen = false },
//            icon = painterResource(id = R.drawable.ic_cancel)
//        )
//    }
//}

@Composable
fun FilePreviewCard(url: String, context: Context, viewModel: PostsViewModel) {
    val fileName = url.substringAfterLast('/')
    val fileType = when {
        url.endsWith(".pdf") -> "PDF File"
        url.endsWith(".xlsx") || url.endsWith(".xls") -> "Excel File"
        url.endsWith(".doc") || url.endsWith(".docx") -> "Word File"
        else -> "File"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.LightGray, RoundedCornerShape(8.dp))
            .clickable {
                // Handle file opening logic
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = getFileTypeIcon(fileType)),
            contentDescription = null,
            modifier = Modifier
                .padding(16.dp)
                .size(40.dp),
            tint = Color.DarkGray
        )
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = fileName,
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = fileType,
                style = TextStyle(color = Color.Gray, fontSize = 12.sp)
            )
        }
        IconButton(
            onClick = {
                viewModel.downloadFile(context, url, fileName)
            }
        ) {
            Icon(
                imageVector = Icons.Default.Download,
                contentDescription = "Download",
                tint = Color.DarkGray
            )
        }
    }
}


fun getFileTypeIcon(fileType: String): Int {
    return when (fileType) {
        "PDF File" -> R.drawable.ic_pdf // Add your PDF icon
        "Excel File" -> R.drawable.ic_excel // Add your Excel icon
        "Word File" -> R.drawable.ic_word // Add your Word icon
        else -> R.drawable.ic_file // Default file icon
    }
}

fun downloadFile(url: String) {
    // Logic to download the file, e.g., using WorkManager or DownloadManager
}