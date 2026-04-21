package com.empresa.scoutbase.data.remote.player

import com.empresa.scoutbase.model.player.PlayerCreateRequest
import com.empresa.scoutbase.model.player.PlayerDeleteResponse
import com.empresa.scoutbase.model.player.PlayerResponse
import com.empresa.scoutbase.model.player.PlayerSingleResponse
import com.empresa.scoutbase.model.player.PlayerUpdateRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface PlayerApi {

    @GET("players/teams/{teamId}")
    suspend fun getPlayersByTeam(
        @Header("Authorization") token: String,
        @Path("teamId") teamId: String
    ): PlayerResponse

    @POST("players/teams/{teamId}")
    suspend fun createPlayer(
        @Header("Authorization") token: String,
        @Path("teamId") teamId: String,
        @Body request: PlayerCreateRequest
    ): PlayerSingleResponse

    @PUT("players/{playerId}")
    suspend fun updatePlayer(
        @Header("Authorization") token: String,
        @Path("playerId") playerId: String,
        @Body request: PlayerUpdateRequest
    ): PlayerSingleResponse

    @DELETE("players/{playerId}")
    suspend fun deletePlayer(
        @Header("Authorization") token: String,
        @Path("playerId") playerId: String
    ): PlayerDeleteResponse
}

