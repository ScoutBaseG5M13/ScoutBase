package com.empresa.scoutbase.data.remote.repository

import com.empresa.scoutbase.data.remote.ApiService
import com.empresa.scoutbase.model.team.Team

class TeamRepositoryImpl : TeamRepository {

    private val api = ApiService.teamApi

    override suspend fun getTeams(token: String): List<Team> {
        val response = api.getTeams("Bearer $token")

        if (response.success) {
            return response.data
        } else {
            throw Exception(response.message)
        }
    }
}





