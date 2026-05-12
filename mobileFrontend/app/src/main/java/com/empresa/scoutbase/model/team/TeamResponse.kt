package com.empresa.scoutbase.model.player

data class TeamResponse(
    val success: Boolean,
    val message: String,
    val data: List<TeamData>,
    val sessionId: String,
    val timestamp: String
)

data class TeamData(
    val id: String,
    val clubId: String,
    val name: String,
    val category: String,
    val subcategory: String,
    val players: List<String>?
)

