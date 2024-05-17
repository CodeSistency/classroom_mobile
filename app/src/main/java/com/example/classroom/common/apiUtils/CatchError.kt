package proyecto.person.appconsultapopular.common.apiUtils


import com.example.classroom.common.apiUtils.GenericCodeFormat
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import proyecto.person.appconsultapopular.common.InvalidException
import proyecto.person.appconsultapopular.common.InvalidExceptionModel

fun catchError(codeApi: Int?, codeInternal: Int?, message: String? = null): InvalidException {
    return when{
        codeApi != null -> {
            val format = GenericCodeFormat.values().find { it.codeApi == codeApi }
            val json = Json.encodeToString(
                InvalidExceptionModel(
                    code = format?.codeApi ?: GenericCodeFormat.INVALID_ERROR.codeInternal!!,
                    isForApi = true,
                    message = message
                )
            )

            InvalidException(json)
        }
        codeInternal != null -> {
            val format = GenericCodeFormat.values().find { it.codeInternal == codeInternal }
            val json = Json.encodeToString(
                InvalidExceptionModel(
                    code = format?.codeInternal ?: GenericCodeFormat.INVALID_ERROR.codeInternal!!,
                    isForApi = false,
                    message = message
                )
            )

            InvalidException(json)
        }
        else -> {
            val json = Json.encodeToString(
                InvalidExceptionModel(
                    code = GenericCodeFormat.INVALID_ERROR.codeInternal!!,
                    isForApi = false,
                    message = message
                )
            )

            InvalidException(json)
        }
    }
}