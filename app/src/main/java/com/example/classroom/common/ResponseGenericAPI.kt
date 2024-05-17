package proyecto.person.appconsultapopular.common

import android.util.Log
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

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

suspend inline fun <reified T> parseResponseToGenericObject(response: HttpResponse, isUsedResponse: Boolean = true): ResponseGenericAPi<T>{
    return when{
        response.status.value < 300 ->{
            val parsedData = if (isUsedResponse) {
                json.decodeFromString<Map<String, String?>>(response.body())["result"]?.let { json.decodeFromString<T>(it) }
            } else {
                null
            }
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