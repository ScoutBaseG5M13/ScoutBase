package com.empresa.scoutbase

import kotlinx.coroutines.runBlocking
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import org.junit.Assert.*
import org.junit.Test

/**
 * TEST 1 — LOGIN CORRECTE
 * Prova funcional que comprova que l'endpoint de login
 * retorna un token vàlid quan les credencials són correctes.
 */
class LoginTest {

    private val client = OkHttpClient()

    @Test
    fun loginCorrecte() = runBlocking {
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

        val responseBody = response.body?.string()
        println("LOGIN STATUS CODE: ${response.code}")
        println("LOGIN RESPONSE BODY: $responseBody")

        assertTrue("El servidor ha retornat un error HTTP: ${response.code}", response.isSuccessful)
        assertNotNull("El body és null", responseBody)

        val jsonResponse = JSONObject(responseBody!!)
        assertTrue("success = false al JSON", jsonResponse.getBoolean("success"))

        val data = jsonResponse.getJSONObject("data")
        val token = data.getString("token")

        assertNotNull("Token null", token)
        assertTrue("Token buit", token.isNotEmpty())
    }
}




