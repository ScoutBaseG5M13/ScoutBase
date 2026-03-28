package com.empresa.scoutbase.data.remote.dto

data class UserByUsernameResponse(
    val success: Boolean,
    val message: String,
    val data: UserData?,
    val sessionId: String?,
    val timestamp: String?
)

data class UserData(
    val id: String,
    val username: String,
    val password: String,
    val role: String
)
