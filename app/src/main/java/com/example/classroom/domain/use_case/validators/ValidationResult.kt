package com.example.classroom.domain.use_case.validators

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: String? = null
)
