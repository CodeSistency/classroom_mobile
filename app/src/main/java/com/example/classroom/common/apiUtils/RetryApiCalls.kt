package com.example.classroom.common.apiUtils

import kotlinx.coroutines.delay

suspend fun <T> retryOperation(
    times: Int = 3,
    delayMillis: Long = 1000L, // Delay between retries
    operation: suspend () -> T
): T {
    var currentAttempt = 0
    while (true) {
        try {
            return operation() // Attempt the operation
        } catch (e: Exception) {
            currentAttempt++
            if (currentAttempt >= times) {
                throw e // If retries are exhausted, throw the exception
            }
            delay(delayMillis) // Wait before retrying
        }
    }
}
