package com.empresa.scoutbase.model.userclub

data class UserClubResponse(
    val id: String,
    val name: String,
    val adminUserIds: List<String>?,
    val userTeams: List<String>?,
    val managedClubs: List<String>?
)