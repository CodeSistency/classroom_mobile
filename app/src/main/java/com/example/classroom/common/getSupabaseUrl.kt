package com.example.classroom.common

fun getSupabaseFileUrl(path: String, isPublic: Boolean = true): String {
    val supabaseBaseUrl = "https://sqsougfscfnaanypvzrl.supabase.co"
    val bucketName = "class_room_documents"

    return if (isPublic) {
        "$supabaseBaseUrl/$path"

//        "$supabaseBaseUrl/storage/v1/object/public/$bucketName/$path"
    } else {
        ""
        // Generate signed URL if the bucket is private
//        val signedUrlResponse = supabaseClient.storage.from(bucketName).createSignedUrl(path, 3600) // Expires in 1 hour
//        signedUrlResponse.data?.signedUrl ?: throw IllegalArgumentException("Failed to get signed URL for $path")
    }
}
