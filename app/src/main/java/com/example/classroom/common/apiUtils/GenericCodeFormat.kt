package com.example.classroom.common.apiUtils

import com.example.classroom.R
import proyecto.person.appconsultapopular.common.AppResource


enum class GenericCodeFormat(val codeApi: Int?, val message: String, val codeInternal: Int?){
    MOVED_PERMANENTLY(
        codeApi = 301,
        message = AppResource.getString(R.string.error_3xx),
        codeInternal = null
    ),
    FOUND(
        codeApi = 302,
        message = AppResource.getString(R.string.error_3xx),
        codeInternal = null
    ),
    SEE_OTHER(
        codeApi = 303,
        message = AppResource.getString(R.string.error_3xx),
        codeInternal = null
    ),
    NOT_MODIFIED(
        codeApi = 304,
        message = AppResource.getString(R.string.error_3xx),
        codeInternal = null
    ),
    TEMPORARY_REDIRECT(
        codeApi = 307,
        message = AppResource.getString(R.string.error_3xx),
        codeInternal = null
    ),
    BAD_REQUEST(
        codeApi = 400,
        message = AppResource.getString(R.string.error_4xx),
        codeInternal = null
    ),
    UNAUTHORIZED(
        codeApi = 401,
        message = AppResource.getString(R.string.error_4xx),
        codeInternal = null
    ),
    FORBIDDEN(
        codeApi = 403,
        message = AppResource.getString(R.string.error_4xx),
        codeInternal = null
    ),
    NOT_FOUND(
        codeApi = 404,
        message = AppResource.getString(R.string.error_4xx),
        codeInternal = null
    ),
    METHOD_NOT_ALLOWED(
        codeApi = 405,
        message = AppResource.getString(R.string.error_4xx),
        codeInternal = null
    ),
    NOT_ACCEPTABLE(
        codeApi = 406,
        message = AppResource.getString(R.string.error_4xx),
        codeInternal = null
    ),
    PRECONDITION_FAILED(
        codeApi = 412,
        message = AppResource.getString(R.string.error_4xx),
        codeInternal = null
    ),
    UNSUPPORTED_MEDIA_TYPE(
        codeApi = 415,
        message = AppResource.getString(R.string.error_4xx),
        codeInternal = null
    ),
    INTERNAL_SERVER_ERROR(
        codeApi = 500,
        message = AppResource.getString(R.string.error_4xx),
        codeInternal = null
    ),
    NOT_IMPLEMENTED(
        codeApi = 501,
        message = AppResource.getString(R.string.error_4xx),
        codeInternal = null
    ),
    BAD_GATEWAY(
        codeApi = 502,
        message = AppResource.getString(R.string.error_4xx),
        codeInternal = null
    ),
    GENERIC_ERROR(
        codeApi = null,
        message = AppResource.getString(R.string.error_4xx),
        codeInternal = 600
    ),
    INVALID_ERROR(
        codeApi = null,
        message = AppResource.getString(R.string.error_4xx),
        codeInternal = 700
    ),
    SERIALIZATION_ERROR(
        codeApi = null,
        message = AppResource.getString(R.string.error_4xx),
        codeInternal = 601
    ),
    TIMEOUT(
        codeApi = null,
        message = AppResource.getString(R.string.error_4xx),
        codeInternal = 705
    )
}