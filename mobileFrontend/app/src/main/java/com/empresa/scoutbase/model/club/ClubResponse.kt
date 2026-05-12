package com.empresa.scoutbase.model.player

data class ClubResponse(
    val success: Boolean,
    val message: String,
    val data: List<ClubData>,
    val sessionId: String,
    val timestamp: String
)

data class ClubData(
    val id: String,
    val name: String,
    val teams: List<String>?
)
