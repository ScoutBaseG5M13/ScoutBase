package com.empresa.scoutbase.model.login

data class ApiResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T?,
    val sessionId: String,
    val timestamp: String
)
