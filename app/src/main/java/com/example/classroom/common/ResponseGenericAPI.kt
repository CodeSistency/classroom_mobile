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
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

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

//suspend inline fun <reified T> parseResponseToGenericObject(
//    response: HttpResponse,
//    isUsedResponse: Boolean = true
//): ResponseGenericAPi<T> {
//    Log.e("RawResponseStatus", response.status.value.toString())
//    val rawBody = response.bodyAsText()
//    Log.e("RawResponseBody", rawBody)
//
//    // Parse the response body as a JsonObject to extract `code` and `message`
//    val json = Json { ignoreUnknownKeys = true }
//    val decodedResponse = json.decodeFromString<JsonObject>(rawBody)
//
//    val responseCode = decodedResponse["code"]?.jsonPrimitive?.intOrNull ?: -1
//    val message = decodedResponse["message"]?.jsonPrimitive?.contentOrNull
//    val dataElement = decodedResponse["data"]
//
//    return try {
//        if (responseCode in 200..299) {
//            // Success based on the `code` in the response
//            ResponseGenericAPi(
//                statusCode = HttpStatusCode.OK, // Always OK for successful cases
//                responseData = if (isUsedResponse) response.body<T>() else null,
//                messageError = null
//            )
//        } else {
//            // Failure based on the `code` in the response
//            ResponseGenericAPi(
//                statusCode = HttpStatusCode.BadRequest, // Use BadRequest for logical errors
//                responseData = null,
//                messageError = ErrorMensaje(responseCode, message ?: "Error desconocido")
//            )
//        }
//    } catch (e: Exception) {
//        Log.e("DataParsingError", "Error parsing response: ${e.message}")
//        ResponseGenericAPi(
//            statusCode = HttpStatusCode.InternalServerError, // Custom fallback
//            responseData = null,
//            messageError = ErrorMensaje(-1, "Error desconocido al procesar la respuesta")
//        )
//    }
//}


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
