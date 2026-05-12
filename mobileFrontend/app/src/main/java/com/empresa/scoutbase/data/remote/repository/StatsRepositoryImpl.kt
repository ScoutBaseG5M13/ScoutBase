package com.empresa.scoutbase.repository

import com.empresa.scoutbase.data.remote.ApiService
import com.empresa.scoutbase.model.stats.CreateStatRequest
import com.empresa.scoutbase.model.stats.PlayerStat
import com.empresa.scoutbase.model.stats.StatDefinition

class StatsRepositoryImpl : StatsRepository {

    private val api = ApiService.statsApi

    override suspend fun getAllStats(token: String): List<StatDefinition> {
        val response = api.getAllStats("Bearer $token")
        return response.data
    }

    override suspend fun getPlayerStats(token: String, playerId: String): List<PlayerStat> {
        val response = api.getPlayerStats("Bearer $token", playerId)
        return response.data
    }

    override suspend fun createPlayerStat(
        token: String,
        playerId: String,
        code: String,
        value: Int
    ): PlayerStat {

        val req = CreateStatRequest(
            code = code,
            value = value
        )

        val response = api.createPlayerStat("Bearer $token", playerId, req)
        return response.data
    }
}





