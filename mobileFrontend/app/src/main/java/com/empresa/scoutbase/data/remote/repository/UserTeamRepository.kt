package com.empresa.scoutbase.data.remote.repository

import com.empresa.scoutbase.model.UserTeam.UserTeam

interface UserTeamRepository {
    suspend fun getUserTeams(token: String, userClubId: String): List<UserTeam>
}






