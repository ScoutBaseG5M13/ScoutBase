package com.empresa.scoutbase

import kotlinx.coroutines.runBlocking
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import org.junit.Assert.*
import org.junit.Test

/**
 * TEST 4 — CREAR JUGADOR
 * Prova funcional que comprova que el servidor permet crear un jugador nou
 * i retorna un ID vàlid.
 */
class CreatePlayerTest {

    private val client = OkHttpClient()

    private fun login(): String {
        val json = """
            {
              "username": "john_doe",
              "password": "password123"

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
        assertTrue(response.isSuccessful)

        val responseBody = response.body?.string()
        val jsonResponse = JSONObject(responseBody!!)
        val data = jsonResponse.getJSONObject("data")

        return data.getString("token")
    }

    @Test
    fun crearJugador() = runBlocking {
        val token = login()

        val teamId = "27d38fed-1d1e-489f-8ab6-a776dbdf6605" // ID real del John's Team

        val json = """
        {
          "name": "JugadorTest",
          "surname": "JUnit",
          "age": 15,
          "email": "junit@test.com",
          "number": 9,
          "position": "DELANTERO_CENTRO",
          "priority": 5
        }
    """.trimIndent()

        val body = RequestBody.create(
            "application/json".toMediaTypeOrNull(),
            json
        )

        val request = Request.Builder()
            .url("https://scoutbase-dev-6r6d.onrender.com/api/v1/players/teams/$teamId")
            .addHeader("Authorization", "Bearer $token")
            .post(body)
            .build()

        val response = client.newCall(request).execute()
        val responseBody = response.body?.string()

        println("CREATE PLAYER STATUS CODE: ${response.code}")
        println("CREATE PLAYER RESPONSE BODY: $responseBody")

        assertTrue("Error HTTP: ${response.code}", response.isSuccessful)
        assertNotNull(responseBody)

        val jsonResponse = JSONObject(responseBody!!)
        assertTrue(jsonResponse.getBoolean("success"))

        val data = jsonResponse.getJSONObject("data")
        val playerId = data.getString("id")

        assertNotNull(playerId)
        assertTrue(playerId.isNotEmpty())
    }
}

