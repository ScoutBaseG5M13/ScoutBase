package com.empresa.scoutbase.data.remote.UserTeam

import com.empresa.scoutbase.model.UserTeam.UserTeamResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface UserTeamApi {

    @GET("user-teams/user-clubs/{id}")
    suspend fun getUserTeamsByClub(
        @Header("Authorization") token: String,
        @Path("id") userClubId: String
    ): UserTeamResponse

}

