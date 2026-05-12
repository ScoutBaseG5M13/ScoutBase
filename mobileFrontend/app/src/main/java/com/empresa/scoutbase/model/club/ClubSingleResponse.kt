package com.empresa.scoutbase.model.club

data class ClubSingleResponse(
    val success: Boolean,
    val message: String,
    val data: ClubData,
    val sessionId: String,
    val timestamp: String
)

data class ClubData(
    val id: String,
    val name: String,
    val teams: List<String>?,   // pot ser null
    val userClub: String        // id del UserClub pare
)


