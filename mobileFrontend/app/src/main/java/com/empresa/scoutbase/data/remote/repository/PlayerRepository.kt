package com.empresa.scoutbase.repository

import com.empresa.scoutbase.model.player.Player
import com.empresa.scoutbase.model.player.PlayerCreateRequest
import com.empresa.scoutbase.model.player.PlayerUpdateRequest

interface PlayerRepository {

    suspend fun getPlayersFromTeam(token: String, teamId: String): List<Player>

    suspend fun createPlayer(token: String, teamId: String, req: PlayerCreateRequest): Player

    suspend fun updatePlayer(token: String, req: PlayerUpdateRequest): Player

    suspend fun deletePlayer(token: String, playerId: String): Boolean
}







