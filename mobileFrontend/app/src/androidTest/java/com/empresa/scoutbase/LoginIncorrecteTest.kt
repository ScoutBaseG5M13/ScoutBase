package com.empresa.scoutbase

import kotlinx.coroutines.runBlocking
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import org.junit.Assert.*
import org.junit.Test

/**
 * TEST 2 — LOGIN INCORRECTE
 * Comprova que el servidor retorna error quan les credencials són incorrectes.
 */
class LoginIncorrectTest {

    private val client = OkHttpClient()

    @Test
    fun loginIncorrecte() = runBlocking {
        val json = """
            {
              "username": "john_doe",
              "password": "contraseña_mal"
            }
        """.trimIndent()

        val body = RequestBody.create(
            "application/json".toMediaTypeOrNull(),
            json
        )

        val request = Request.Builder()
            .url("https://scoutbase-dev-6r6d.onrender.com/api/v1/users/auth/login")
            .post(body)
            .build()

        val response = client.newCall(request).execute()

        // El login incorrecte NO ha de ser successful
        assertFalse(response.isSuccessful)

        val responseBody = response.body?.string()
        assertNotNull(responseBody)

        val jsonResponse = JSONObject(responseBody!!)
        assertFalse(jsonResponse.getBoolean("success"))
    }
}


