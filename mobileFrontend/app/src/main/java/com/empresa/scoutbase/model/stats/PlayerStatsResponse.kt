package com.empresa.scoutbase.model.stats

data class PlayerStatsResponse(
    val success: Boolean,
    val message: String,
    val data: List<PlayerStat>,
    val sessionId: String,
    val timestamp: String
)


