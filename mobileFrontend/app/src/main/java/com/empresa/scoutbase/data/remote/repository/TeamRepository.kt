package com.empresa.scoutbase.data.remote.repository

import com.empresa.scoutbase.model.team.Team

interface TeamRepository {
    suspend fun getTeams(token: String): List<Team>
}



