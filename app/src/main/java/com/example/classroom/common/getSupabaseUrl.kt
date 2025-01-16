package com.example.classroom.common

import android.util.Log
import proyecto.person.appconsultapopular.common.Constants

fun getSupabaseFileUrl(path: String, isPublic: Boolean = true, useSupabase: Boolean): String {
    val supabaseBaseUrl = "https://sqsougfscfnaanypvzrl.supabase.co"
    val baseUrl = "${Constants.BASE_URL}"

    Log.e("files url", "${baseUrl}${path}")

    val bucketName = "class_room_documents"

    return if (useSupabase) {
        "$supabaseBaseUrl/storage/v1/object/public/$path"
        if (isPublic) {
            "$supabaseBaseUrl/storage/v1/object/public/$path"

//        "$supabaseBaseUrl/storage/v1/object/public/$bucketName/$path"
        } else {
            ""
            // Generate signed URL if the bucket is private
//        val signedUrlResponse = supabaseClient.storage.from(bucketName).createSignedUrl(path, 3600) // Expires in 1 hour
//        signedUrlResponse.data?.signedUrl ?: throw IllegalArgumentException("Failed to get signed URL for $path")
        }
//        "$supabaseBaseUrl/storage/v1/object/public/$bucketName/$path"
    } else {
        "${path}"

    }
}
