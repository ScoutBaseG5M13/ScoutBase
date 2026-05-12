package com.empresa.scoutbase.model.stats

data class CreateStatResponse(
    val success: Boolean,
    val message: String,
    val data: PlayerStat,
    val sessionId: String,
    val timestamp: String
)
