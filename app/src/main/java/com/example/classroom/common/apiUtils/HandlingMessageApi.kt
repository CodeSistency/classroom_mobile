package proyecto.person.appconsultapopular.common.apiUtils

import com.example.classroom.common.apiUtils.GenericCodeFormat

fun handlingMessageApi(format: GenericCodeFormat, message: String? = null): GenericCodeModel {
    val nomenclature = setNomenclature(
        codeApi = format.codeApi,
        codeInternal = format.codeInternal
    )
    return GenericCodeModel(
        uiMessage = message ?: format.message,
        nomenclature = nomenclature
    )
}

private fun setNomenclature(codeApi: Int?, codeInternal: Int?): String{
    val codeFormatApi = codeApi?.toString() ?: "000"
    val codeFormatInternal = codeInternal?.toString() ?: "000"

    return "FGT.$codeFormatApi-$codeFormatInternal"
}

data class GenericCodeModel(
    val uiMessage: String,
    val nomenclature: String
)