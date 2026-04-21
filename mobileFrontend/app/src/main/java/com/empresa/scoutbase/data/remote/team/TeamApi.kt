package com.empresa.scoutbase.data.remote.team

import com.empresa.scoutbase.model.team.TeamResponse
import retrofit2.http.GET
import retrofit2.http.Header

interface TeamApi {

    @GET("teams")
    suspend fun getTeams(
        @Header("Authorization") token: String
    ): TeamResponse
}

