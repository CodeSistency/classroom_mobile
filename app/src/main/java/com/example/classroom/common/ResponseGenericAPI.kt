package com.example.classroom.common

import android.util.Log
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject

val json = Json {
    coerceInputValues = true
    ignoreUnknownKeys = true
}
data class ResponseGenericAPi<T>(
    val statusCode: HttpStatusCode,
    val responseData: T?,
    val messageError: ErrorMensaje?
)

suspend inline fun <reified T> parseResponseToGenericObject(response: HttpResponse, isUsedResponse: Boolean = true): ResponseGenericAPi<T>{
    Log.e("RawResponseStatus", response.status.value.toString()) // Log raw response

    return when{

        response.status.value < 300 ->{
            Log.e("RawResponseBody", response.bodyAsText()) // Log raw response

            ResponseGenericAPi(
                statusCode = response.status,
                responseData = if (isUsedResponse) response.body<T>() else null,
                messageError = null
            )
        }
        else -> {
            ResponseGenericAPi(
                statusCode = response.status,
                responseData = null,
                messageError = response.body<ErrorMensaje>()
            )
        }
    }
}

@Serializable
data class ErrorMensaje(
    @SerialName("code")
    val code: Int,
    @SerialName("message")
    val message: String,
)



//val json = Json {
//    coerceInputValues = true
//    ignoreUnknownKeys = true
//}
//data class ResponseGenericAPi<T>(
//    val statusCode: HttpStatusCode,
//    val responseData: T?,
//    val messageError: ErrorMensaje?
//)
//
//suspend inline fun <reified T> parseResponseToGenericObject(response: HttpResponse, isUsedResponse: Boolean = true): ResponseGenericAPi<T>{
//    Log.e("RawResponseStatus", response.status.value.toString()) // Log raw response
//
//    return when{
//
//        response.status.value < 300 ->{
//            Log.e("RawResponseBody", response.bodyAsText()) // Log raw response
//
//            ResponseGenericAPi(
//                statusCode = response.status,
//                responseData = if (isUsedResponse) response.body<T>() else null,
//                messageError = null
//            )
//        }
//        else -> {
//            ResponseGenericAPi(
//                statusCode = response.status,
//                responseData = null,
//                messageError = response.body<ErrorMensaje>()
//            )
//        }
//    }
//}
//
//@Serializable
//data class ErrorMensaje(
//    @SerialName("code")
//    val code: Int,
//    @SerialName("message")
//    val message: String,
//)
