package com.empresa.scoutbase.data.remote.login

import com.empresa.scoutbase.model.login.ApiResponse
import com.empresa.scoutbase.model.login.LoginRequest
import com.empresa.scoutbase.model.login.LoginResponse
import com.empresa.scoutbase.model.userclub.UserClubResponse
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

    @GET("user-clubs")
    suspend fun getUserClubs(
        @Header("Authorization") token: String
    ): ApiResponse<List<UserClubResponse>>

    @GET("users/user-clubs/{id}/role")
    suspend fun getUserRoleInsideClub(
        @Header("Authorization") token: String,
        @Path("id") userClubId: String
    ): ApiResponse<String>
}







