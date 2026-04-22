package com.empresa.scoutbase.model.player

data class PlayerSingleResponse(
    val success: Boolean,
    val message: String,
    val data: Player,
    val sessionId: String,
    val timestamp: String
)


