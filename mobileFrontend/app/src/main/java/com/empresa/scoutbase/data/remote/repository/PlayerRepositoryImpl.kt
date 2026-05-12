package com.empresa.scoutbase.repository

import com.empresa.scoutbase.data.remote.ApiService
import com.empresa.scoutbase.model.player.Player
import com.empresa.scoutbase.model.player.PlayerCreateRequest
import com.empresa.scoutbase.model.player.PlayerUpdateRequest

class PlayerRepositoryImpl : PlayerRepository {

    private val api = ApiService.playerApi

    override suspend fun getPlayersFromTeam(token: String, teamId: String): List<Player> {
        val response = api.getPlayersFromTeam("Bearer $token", teamId)
        return response.data
    }

    override suspend fun createPlayer(token: String, teamId: String, req: PlayerCreateRequest): Player {
        val response = api.createPlayer("Bearer $token", teamId, req)
        return response.data
    }

    override suspend fun updatePlayer(token: String, req: PlayerUpdateRequest): Player {
        val response = api.updatePlayer("Bearer $token", req.id, req)
        return response.data
    }

    override suspend fun deletePlayer(token: String, playerId: String): Boolean {
        val response = api.deletePlayer("Bearer $token", playerId)
        return response.data
    }
}



