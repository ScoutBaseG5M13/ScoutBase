package com.empresa.scoutbase.repository

import com.empresa.scoutbase.model.stats.PlayerStat
import com.empresa.scoutbase.model.stats.StatDefinition

interface StatsRepository {

    suspend fun getAllStats(token: String): List<StatDefinition>

    suspend fun getPlayerStats(token: String, playerId: String): List<PlayerStat>

    suspend fun createPlayerStat(
        token: String,
        playerId: String,
        code: String,
        value: Int
    ): PlayerStat
}
