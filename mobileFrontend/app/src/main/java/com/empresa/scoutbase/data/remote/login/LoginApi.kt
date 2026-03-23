package com.empresa.scoutbase.data.remote.login

import com.empresa.scoutbase.model.login.ApiResponse
import com.empresa.scoutbase.model.login.LoginRequest
import com.empresa.scoutbase.model.login.LoginResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * Interfaz Retrofit que define las peticiones relacionadas con la autenticación
 * y obtención del rol del usuario.
 */
interface LoginApi {

    /**
     * Endpoint de login.
     * Envía usuario y contraseña y devuelve un token si las credenciales son correctas.
     */
    @POST("users/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): ApiResponse<LoginResponse>

    /**
     * Endpoint para obtener el rol del usuario autenticado.
     * Requiere enviar el token en el header Authorization.
     *
     * Respuesta esperada:
     * {
     *   "success": true,
     *   "data": "ROLE_USER"
     * }
     */
    @GET("users/role")
    suspend fun getUserRole(
        @Header("Authorization") token: String
    ): ApiResponse<String>
}






