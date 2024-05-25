package com.example.classroom.common

import android.util.Log
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject

//data class ResponseGenericAPi<T>(
//    val statusCode: HttpStatusCode,
//    val responseData: T?,
//    val messageError: String?,
//    val loading: Boolean = false,
//)


//suspend inline fun <reified T> parseResponseToGenericObject(response: HttpResponse, isUsedResponse: Boolean = true): ResponseGenericAPi<out T> {
//    var loading = true // Set loading to true initially
//
//    return runCatching {
//        ResponseGenericAPi(
//            statusCode = response.status,
//            responseData = if (isUsedResponse) response.body<T>() else null,
//            messageError = null,
//            loading = loading // Include loading state
//        )
//    }.getOrElse { e ->
//        ResponseGenericAPi(
//            statusCode = response.status,
//            responseData = null,
//            messageError = e.message,
//            loading = false // Set loading to false in case of error
//        )
//    }.also {
//        loading = false // Set loading to false after response retrieval
//    }
//}

data class ResponseGenericAPi<T>(
    val statusCode: HttpStatusCode,
    val responseData: T?,
    val messageError: String?,
)

val json = Json {
    coerceInputValues = true
    ignoreUnknownKeys = true
}

suspend inline fun <reified T> parseResponseToGenericObject(response: HttpResponse, isUsedResponse: Boolean = true): ResponseGenericAPi<T> {
//    Log.e("RESPONSE", response.bodyAsText())
    return when{
        response.status.value < 300 ->{

            Log.e("iscorrect", "iscorrect")
            Log.e("data response", "${response.bodyAsText()}")

            val parsedData = if (isUsedResponse) {
                try {
                    val responseBody = response.bodyAsText()
                    Log.e("RESPONSE", responseBody)
                    // Parse the entire response as a JsonObject
                    val jsonObject = json.parseToJsonElement(responseBody).jsonObject
                    // Access the "result" field and convert it to string
                    val resultJson = jsonObject["result"]?.toString()

                    // Log the intermediate JSON string for debugging
                    Log.e("resultJson", resultJson ?: "null")

                    // Deserialize the resultJson to the expected generic type T
                    resultJson?.let { json.decodeFromString<T>(it) }
                } catch (e: Exception) {
                    Log.e("JsonError", "Error decoding response: ${e.message}")
                    null
                }
            } else {
                null
            }
            Log.e("parsed data", parsedData.toString())
            ResponseGenericAPi(
                statusCode = response.status,
                responseData = parsedData,
                messageError = null
            )
        }
        else -> {
            ResponseGenericAPi(
                statusCode = response.status,
                responseData = null,
                messageError = response.body<String>()
            )
        }
    }
}