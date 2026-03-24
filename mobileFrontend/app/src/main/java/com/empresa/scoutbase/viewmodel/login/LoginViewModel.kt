package com.empresa.scoutbase.viewmodel.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empresa.scoutbase.data.remote.ApiService
import com.empresa.scoutbase.data.remote.login.LoginApi
import com.empresa.scoutbase.model.login.LoginRequest
import com.empresa.scoutbase.model.login.LoginResponse
import com.empresa.scoutbase.model.login.ApiResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel responsable de gestionar el procés d'autenticació de l'usuari.
 *
 * Funcionalitats principals:
 * - Enviar les credencials al backend.
 * - Rebre i emmagatzemar el token si el login és correcte.
 * - Obtenir el rol real de l'usuari mitjançant l'endpoint `/users/role`.
 * - Gestionar l'estat de càrrega i els missatges d'error.
 */
class LoginViewModel : ViewModel() {

    /** Instància de Retrofit per accedir a les operacions de login. */
    private val api = ApiService.retrofit.create(LoginApi::class.java)

    /** Estat que indica si hi ha una operació de càrrega en curs. */
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    /** Missatge d'error actual, si n'hi ha. */
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    /** Token rebut després d'un login correcte. */
    private val _token = MutableStateFlow<String?>(null)
    val token: StateFlow<String?> = _token

    /** Rol real de l'usuari autenticat (ROLE_ADMIN o ROLE_USER). */
    private val _role = MutableStateFlow<String?>(null)
    val role: StateFlow<String?> = _role

    /**
     * Realitza el procés complet de login.
     *
     * Passos:
     * 1. Envia les credencials a `/users/auth/login`.
     * 2. Si són correctes, rep un token i l'emmagatzema.
     * 3. Amb el token, consulta el rol real de l'usuari a `/users/role`.
     *
     * @param username Nom d'usuari introduït.
     * @param password Contrasenya introduïda.
     */
    fun login(username: String, password: String) {
        _loading.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                val request = LoginRequest(username, password)
                val response: ApiResponse<LoginResponse> = api.login(request)

                if (response.success && response.data != null) {
                    val token = response.data.token
                    _token.value = token

                    val roleResponse = api.getUserRole("Bearer $token")

                    if (roleResponse.success && roleResponse.data != null) {
                        _role.value = roleResponse.data
                    } else {
                        _error.value = "No se pudo obtener el rol del usuario"
                    }

                } else {
                    _error.value = response.message
                }

            } catch (e: Exception) {
                e.printStackTrace()
                _error.value = "Error real: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    /**
     * Assigna un missatge d'error manualment.
     *
     * @param msg Missatge d'error que es vol mostrar.
     */
    fun setError(msg: String) {
        _error.value = msg
    }

    /**
     * Realitza el procés de logout.
     *
     * Accions:
     * - Esborra el token de l'usuari.
     * - Esborra el rol assignat.
     * - Reinicia l'estat d'error.
     *
     * Aquesta funció no fa cap crida al backend; només neteja l'estat local.
     */
    fun logout() {
        _token.value = null
        _role.value = null
        _error.value = null
    }

    /**
     * Funció utilitzada exclusivament per a proves unitàries.
     * Permet simular que l'usuari està autenticat assignant un token i un rol.
     *
     * @param token Token fictici per simular autenticació.
     * @param role Rol fictici assignat a l'usuari.
     */
    fun setFakeAuth(token: String, role: String) {
        _token.value = token
        _role.value = role
    }
}








