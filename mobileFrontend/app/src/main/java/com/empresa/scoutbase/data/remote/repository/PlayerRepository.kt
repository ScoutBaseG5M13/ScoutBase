package com.empresa.scoutbase.data.remote.repository

import com.empresa.scoutbase.model.player.Player
import com.empresa.scoutbase.model.player.PlayerCreateRequest
import com.empresa.scoutbase.model.player.PlayerUpdateRequest

interface PlayerRepository {
    suspend fun getPlayersByTeam(token: String, teamId: String): List<Player>
    suspend fun createPlayer(token: String, teamId: String, request: PlayerCreateRequest): Player
    suspend fun updatePlayer(token: String, playerId: String, request: PlayerUpdateRequest): Player
    suspend fun deletePlayer(token: String, playerId: String): Boolean
}






