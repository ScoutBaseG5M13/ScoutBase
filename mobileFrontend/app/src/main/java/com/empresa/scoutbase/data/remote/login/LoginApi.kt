package com.empresa.scoutbase.data.remote.login

import com.empresa.scoutbase.data.remote.dto.UserByUsernameResponse
import com.empresa.scoutbase.model.login.ApiResponse
import com.empresa.scoutbase.model.login.LoginRequest
import com.empresa.scoutbase.model.login.LoginResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface LoginApi {

    @POST("users/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): ApiResponse<LoginResponse>

    @GET("users/username/{username}")
    suspend fun getUserByUsername(
        @Header("Authorization") token: String,
        @Path("username") username: String
    ): UserByUsernameResponse
}





