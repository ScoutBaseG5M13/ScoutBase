package com.empresa.scoutbase

import kotlinx.coroutines.runBlocking
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import org.junit.Assert.*
import org.junit.Test

/**
 * TEST 3 — OBTENIR EQUIPS
 * Prova funcional que comprova que el servidor retorna la llista d'equips
 * d'un usuari després de fer login correctament.
 */
class GetTeamsTest {

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
    fun obtenirEquips() = runBlocking {
        val token = login()

        val request = Request.Builder()
            .url("https://scoutbase-dev-6r6d.onrender.com/api/v1/teams")
            .addHeader("Authorization", "Bearer $token")
            .get()
            .build()

        val response = client.newCall(request).execute()
        assertTrue(response.isSuccessful)

        val responseBody = response.body?.string()
        assertNotNull(responseBody)

        val jsonResponse = JSONObject(responseBody!!)
        assertTrue(jsonResponse.getBoolean("success"))

        val data = jsonResponse.getJSONArray("data")

        // No importa si està buida o no, només que sigui un array vàlid
        assertNotNull(data)
    }
}


