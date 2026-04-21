package com.empresa.scoutbase.data.remote.repository

import com.empresa.scoutbase.data.remote.ApiService
import com.empresa.scoutbase.model.player.*

class PlayerRepositoryImpl : PlayerRepository {

    private val api = ApiService.playerApi

    override suspend fun getPlayersByTeam(token: String, teamId: String): List<Player> {
        val response = api.getPlayersByTeam("Bearer $token", teamId)
        if (response.success) return response.data
        else throw Exception(response.message)
    }

    override suspend fun createPlayer(token: String, teamId: String, request: PlayerCreateRequest): Player {
        val response = api.createPlayer("Bearer $token", teamId, request)
        if (response.success) return response.data
        else throw Exception(response.message)
    }

    override suspend fun updatePlayer(token: String, playerId: String, request: PlayerUpdateRequest): Player {
        val response = api.updatePlayer("Bearer $token", playerId, request)
        if (response.success) return response.data
        else throw Exception(response.message)
    }

    override suspend fun deletePlayer(token: String, playerId: String): Boolean {
        val response = api.deletePlayer("Bearer $token", playerId)
        if (response.success) return response.data
        else throw Exception(response.message)
    }
}


