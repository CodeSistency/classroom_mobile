package com.example.classroom.common.composables.PreviewFile

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log

fun getMimeTypeFromExtension(extension: String): String {
    return when (extension.lowercase()) {
        "jpg", "jpeg" -> "image/jpeg"
        "png" -> "image/png"
        "gif" -> "image/gif"
        "webp" -> "image/webp"
        "pdf" -> "application/pdf"
        "doc", "docx" -> "application/msword"
        "xls", "xlsx" -> "application/vnd.ms-excel"
        // Agrega más extensiones / mime types según tu necesidad
        else -> "*/*"
    }
}

fun getDownloadStatusAndReason(context: Context, downloadId: Long): Pair<Int, Int> {
    val dm = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    val query = DownloadManager.Query().setFilterById(downloadId)
    dm.query(query).use { cursor ->
        if (cursor != null && cursor.moveToFirst()) {
            val status = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS))
            val reason = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_REASON))
            return status to reason
        }
    }
    // Default to failed if no record
    return DownloadManager.STATUS_FAILED to -1
}


fun getDownloadStatus(context: Context, downloadId: Long): Int {
    val dm = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    val query = DownloadManager.Query().setFilterById(downloadId)
    dm.query(query).use { cursor ->
        if (cursor != null && cursor.moveToFirst()) {
            val status = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS))
            val reason = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_REASON))
            Log.e("DownloadManager", "Status: $status, Reason: $reason")
            return status
        }
    }
    return DownloadManager.STATUS_FAILED
}

fun enqueueDownload(
    context: Context,
    url: String,
    fileName: String,
    mimeType: String = "*/*"
): Long {
    val request = DownloadManager.Request(Uri.parse(url)).apply {
        setTitle(fileName)
        setDescription("Descargando archivo...")
        setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
        setMimeType(mimeType)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Android 10+: Use private external dir
            setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, fileName)
        } else {
            // Older devices can still use public
            setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
        }
    }

    val dm = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    return dm.enqueue(request)
}
//fun enqueueDownload(
//    context: Context,
//    fileUrl: String,
//    fileName: String,
//    mimeType: String = "*/*"
//): Long {
//    val request = DownloadManager.Request(Uri.parse(fileUrl)).apply {
//        setTitle(fileName)
//        setDescription("Descargando archivo...")
//
//        // Show a notification while downloading and after completion
//        setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
//
//        // Allow both Wi-Fi and mobile networks
//        setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
//
//        // Set MIME type
//        setMimeType(mimeType)
//
//        // Use different destinations based on API level
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            // Android 10+ -> scoped storage approach
//            // Puts file in: /storage/emulated/0/Android/data/<package>/files/Download/<fileName>
//            setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, fileName)
//        } else {
//            // For older devices, public directory is still okay
//            setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
//        }
//    }
//
//    val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
//    return downloadManager.enqueue(request)
//}

fun getLocalUriFromDownloadId(context: Context, downloadId: Long): Uri? {
    val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    val query = DownloadManager.Query().apply { setFilterById(downloadId) }
    val cursor: Cursor? = downloadManager.query(query)

    var uri: Uri? = null
    cursor?.use {
        if (it.moveToFirst()) {
            val columnIndex = it.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)
            if (columnIndex != -1) {
                val uriString = it.getString(columnIndex)
                if (!uriString.isNullOrEmpty()) {
                    uri = Uri.parse(uriString)
                }
            }
        }
    }
    return uri
}

fun openFileWithIntent(context: Context, fileUri: Uri, mimeType: String) {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(fileUri, mimeType)
        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK
    }
    context.startActivity(intent)
}