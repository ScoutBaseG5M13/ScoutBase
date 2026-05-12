package com.empresa.scoutbase.data.remote.repository

import com.empresa.scoutbase.data.remote.ApiService
import com.empresa.scoutbase.model.UserTeam.UserTeam

class UserTeamRepositoryImpl : UserTeamRepository {

    private val api = ApiService.userTeamApi

    override suspend fun getUserTeams(token: String, userClubId: String): List<UserTeam> {
        val response = api.getUserTeamsByClub("Bearer $token", userClubId)

        if (response.success) {
            return response.data
        } else {
            throw Exception(response.message)
        }
    }
}






