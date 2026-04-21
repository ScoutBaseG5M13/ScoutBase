package com.empresa.scoutbase.data.remote.user

import com.empresa.scoutbase.data.remote.dto.UserByUsernameResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface UserApi {

    @GET("users/username/{username}")
    suspend fun getUserByUsername(
        @Header("Authorization") token: String,
        @Path("username") username: String
    ): UserByUsernameResponse
}


