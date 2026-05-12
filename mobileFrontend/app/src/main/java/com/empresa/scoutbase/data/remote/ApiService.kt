package com.empresa.scoutbase.data.remote

import com.empresa.scoutbase.data.remote.UserTeam.UserTeamApi
import com.empresa.scoutbase.data.remote.login.LoginApi
import com.empresa.scoutbase.data.remote.player.PlayerApi
import com.empresa.scoutbase.data.remote.stats.StatsApi
import com.empresa.scoutbase.data.remote.user.UserApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiService {

    private const val BASE_URL = "https://scoutbase-dev-6r6d.onrender.com/api/v1/"

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val loginApi: LoginApi = retrofit.create(LoginApi::class.java)
    val userTeamApi: UserTeamApi = retrofit.create(UserTeamApi::class.java)
    val userApi: UserApi = retrofit.create(UserApi::class.java)
    val playerApi: PlayerApi = retrofit.create(PlayerApi::class.java)
    val statsApi: StatsApi = retrofit.create(StatsApi::class.java)
}






