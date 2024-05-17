package proyecto.person.appconsultapopular.common

import kotlinx.serialization.Serializable

class InvalidException(jsonData: String): Exception(jsonData)

@Serializable
data class InvalidExceptionModel(
    val code: Int,
    val isForApi: Boolean,
    val message: String? = null
)