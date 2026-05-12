package com.empresa.scoutbase.model.UserTeam

data class UserTeamResponse(
    val success: Boolean,
    val message: String,
    val data: List<UserTeam>,
    val sessionId: String,
    val timestamp: String
)

