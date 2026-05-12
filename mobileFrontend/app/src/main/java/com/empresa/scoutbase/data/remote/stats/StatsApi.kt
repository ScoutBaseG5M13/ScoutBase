package com.empresa.scoutbase.data.remote.stats

import com.empresa.scoutbase.model.stats.CreateStatRequest
import com.empresa.scoutbase.model.stats.CreateStatResponse
import com.empresa.scoutbase.model.stats.PlayerStatsResponse
import com.empresa.scoutbase.model.stats.StatDefinition
import com.empresa.scoutbase.model.stats.StatsDefinitionResponse
import retrofit2.http.*

interface StatsApi {

    @GET("stats")
    suspend fun getAllStats(
        @Header("Authorization") token: String
    ): StatsDefinitionResponse

    @GET("stats/players/{id}")
    suspend fun getPlayerStats(
        @Header("Authorization") token: String,
        @Path("id") playerId: String
    ): PlayerStatsResponse

    @POST("players/{id}/stats")
    suspend fun createPlayerStat(
        @Header("Authorization") token: String,
        @Path("id") playerId: String,
        @Body request: CreateStatRequest
    ): CreateStatResponse
}


