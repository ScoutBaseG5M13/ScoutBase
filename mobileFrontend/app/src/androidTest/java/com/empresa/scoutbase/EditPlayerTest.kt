package com.empresa.scoutbase

import kotlinx.coroutines.runBlocking
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import org.junit.Assert.*
import org.junit.Test

/**
 * TEST 5 — EDITAR JUGADOR
 * Prova funcional que comprova que es pot editar un jugador existent
 * i que el servidor retorna el resultat correctament.
 */
class EditPlayerTest {

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
              "name": "JugadorOriginal",
              "surname": "Test",
              "age": 14,
              "email": "original@test.com",
              "number": 7,
              "position": "DELANTERO_CENTRO",
              "priority": 3
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
    fun editarJugador() = runBlocking {
        val token = login()
        val playerId = crearJugador(token)

        val json = """
            {
              "id": "$playerId",
              "name": "JugadorEditado",
              "surname": "Modificat",
              "age": 16,
              "email": "editado@test.com",
              "number": 10,
              "position": "MEDIAPUNTA",
              "priority": 8
            }
        """.trimIndent()

        val body = RequestBody.create(
            "application/json".toMediaTypeOrNull(),
            json
        )

        val request = Request.Builder()
            .url("https://scoutbase-dev-6r6d.onrender.com/api/v1/players/$playerId")
            .addHeader("Authorization", "Bearer $token")
            .put(body)
            .build()

        val response = client.newCall(request).execute()
        val responseBody = response.body?.string()

        println("EDIT PLAYER STATUS: ${response.code}")
        println("EDIT PLAYER BODY: $responseBody")

        assertTrue(response.isSuccessful)
        assertNotNull(responseBody)

        val jsonResponse = JSONObject(responseBody!!)
        assertTrue(jsonResponse.getBoolean("success"))

        val data = jsonResponse.getJSONObject("data")

        assertEquals("JugadorEditado", data.getString("name"))
        assertEquals("Modificat", data.getString("surname"))
        assertEquals(16, data.getInt("age"))
        assertEquals("editado@test.com", data.getString("email"))
        assertEquals(10, data.getInt("number"))
        assertEquals("MEDIAPUNTA", data.getString("position"))
        assertEquals(8, data.getInt("priority"))
    }
}



