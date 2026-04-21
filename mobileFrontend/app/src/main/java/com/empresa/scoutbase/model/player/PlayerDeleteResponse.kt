package com.empresa.scoutbase.model.player

data class PlayerDeleteResponse(
    val success: Boolean,
    val message: String,
    val data: Boolean,
    val sessionId: String,
    val timestamp: String
)
