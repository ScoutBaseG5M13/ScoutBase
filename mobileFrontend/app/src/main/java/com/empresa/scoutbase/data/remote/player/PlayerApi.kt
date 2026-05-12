package com.empresa.scoutbase.data.remote.player

import com.empresa.scoutbase.model.club.ClubSingleResponse
import com.empresa.scoutbase.model.player.ClubCreateRequest
import com.empresa.scoutbase.model.player.PlayerCreateRequest
import com.empresa.scoutbase.model.player.PlayerDeleteResponse
import com.empresa.scoutbase.model.player.PlayerResponse
import com.empresa.scoutbase.model.player.PlayerSingleResponse
import com.empresa.scoutbase.model.player.PlayerUpdateRequest
import com.empresa.scoutbase.model.player.TeamResponse
import com.empresa.scoutbase.model.player.ClubResponse
import com.empresa.scoutbase.model.player.TeamCreateRequest
import com.empresa.scoutbase.model.team.TeamSingleResponse
import retrofit2.http.*

interface PlayerApi {

    // 1) Obtener Clubs desde un UserClub
    @GET("clubs/user-clubs/{id}")
    suspend fun getClubsFromUserClub(
        @Header("Authorization") token: String,
        @Path("id") userClubId: String
    ): ClubResponse

    // 2) Obtener Teams desde un Club
    @GET("teams/clubs/{id}")
    suspend fun getTeamsFromClub(
        @Header("Authorization") token: String,
        @Path("id") clubId: String
    ): TeamResponse

    // 3) Obtener Players desde un Team
    @GET("players/teams/{id}")
    suspend fun getPlayersFromTeam(
        @Header("Authorization") token: String,
        @Path("id") teamId: String
    ): PlayerResponse

    // 4) Crear Player dentro de un Team
    @POST("teams/{id}/players")
    suspend fun createPlayer(
        @Header("Authorization") token: String,
        @Path("id") teamId: String,
        @Body request: PlayerCreateRequest
    ): PlayerSingleResponse

    // 5) Editar Player
    @PUT("players/{id}")
    suspend fun updatePlayer(
        @Header("Authorization") token: String,
        @Path("id") playerId: String,
        @Body request: PlayerUpdateRequest
    ): PlayerSingleResponse

    // 6) Eliminar Player
    @DELETE("players/{id}")
    suspend fun deletePlayer(
        @Header("Authorization") token: String,
        @Path("id") playerId: String
    ): PlayerDeleteResponse

    @POST("user-clubs/{id}/clubs")
    suspend fun createClubInUserClub(
        @Header("Authorization") token: String,
        @Path("id") userClubId: String,
        @Body req: ClubCreateRequest
    ): ClubSingleResponse

    @POST("clubs/{id}/teams")
    suspend fun createTeamInClub(
        @Header("Authorization") token: String,
        @Path("id") clubId: String,
        @Body req: TeamCreateRequest
    ): TeamSingleResponse


}



