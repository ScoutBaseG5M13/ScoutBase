package com.empresa.scoutbase.model.team

data class TeamResponse(
    val success: Boolean,
    val message: String,
    val data: List<Team>,
    val sessionId: String,
    val timestamp: String
)
