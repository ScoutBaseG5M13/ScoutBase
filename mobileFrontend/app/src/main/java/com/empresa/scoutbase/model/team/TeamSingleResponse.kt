package com.empresa.scoutbase.model.team

data class TeamSingleResponse(
    val success: Boolean,
    val message: String,
    val data: TeamData,
    val sessionId: String,
    val timestamp: String
)

data class TeamData(
    val id: String,
    val clubId: String,
    val name: String,
    val category: String,
    val subcategory: String,
    val players: List<String>?   // pot ser null
)


