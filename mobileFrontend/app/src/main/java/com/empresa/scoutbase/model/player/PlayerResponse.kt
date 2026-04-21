package com.empresa.scoutbase.model.player

data class PlayerResponse(
    val success: Boolean,
    val message: String,
    val data: List<Player>,
    val sessionId: String,
    val timestamp: String
)
