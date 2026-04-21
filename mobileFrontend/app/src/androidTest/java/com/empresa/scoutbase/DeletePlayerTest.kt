package com.empresa.scoutbase

import kotlinx.coroutines.runBlocking
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import org.junit.Assert.*
import org.junit.Test

/**
 * TEST 6 — ELIMINAR JUGADOR
 * Prova funcional que comprova que es pot eliminar un jugador existent
 * i que el servidor retorna success = true.
 */
class DeletePlayerTest {

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

    private fun crearJugador(token: String): String {
        val teamId = "27d38fed-1d1e-489f-8ab6-a776dbdf6605" // Team real de John

        val json = """
            {
              "name": "JugadorEliminar",
              "surname": "Test",
              "age": 13,
              "email": "delete@test.com",
              "number": 11,
              "position": "DELANTERO_CENTRO",
              "priority": 2
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

        println("CREATE PLAYER STATUS: ${response.code}")
        println("CREATE PLAYER BODY: $responseBody")

        assertTrue("Error creant jugador: ${response.code}", response.isSuccessful)

        val jsonResponse = JSONObject(responseBody!!)
        val data = jsonResponse.getJSONObject("data")

        return data.getString("id")
    }

    @Test
    fun eliminarJugador() = runBlocking {
        val token = login()
        val playerId = crearJugador(token)

        val request = Request.Builder()
            .url("https://scoutbase-dev-6r6d.onrender.com/api/v1/players/$playerId")
            .addHeader("Authorization", "Bearer $token")
            .delete()
            .build()

        val response = client.newCall(request).execute()
        val responseBody = response.body?.string()

        println("DELETE STATUS: ${response.code}")
        println("DELETE BODY: $responseBody")

        assertTrue(response.isSuccessful)
        assertNotNull(responseBody)

        val jsonResponse = JSONObject(responseBody!!)
        assertTrue(jsonResponse.getBoolean("success"))
    }
}



