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
 * ViewModel encargado de gestionar el proceso de login.
 * 1. Envía usuario/contraseña al backend.
 * 2. Recibe token si es correcto.
 * 3. Obtiene el rol real del usuario mediante el endpoint /users/role.
 */
class LoginViewModel : ViewModel() {

    // Instancia de Retrofit → LoginApi
    private val api = ApiService.retrofit.create(LoginApi::class.java)

    // Estado de carga (spinner)
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    // Estado de error
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // Token recibido tras login
    private val _token = MutableStateFlow<String?>(null)
    val token: StateFlow<String?> = _token

    // Rol del usuario (ROLE_ADMIN o ROLE_USER)
    private val _role = MutableStateFlow<String?>(null)
    val role: StateFlow<String?> = _role

    /**
     * Función principal de login.
     * 1. /users/auth/login → token
     * 2. /users/role → rol real del usuario
     */
    fun login(username: String, password: String) {
        _loading.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                // Construimos la petición de login
                val request = LoginRequest(username, password)

                // 1. Llamada al login
                val response: ApiResponse<LoginResponse> = api.login(request)

                if (response.success && response.data != null) {

                    // Guardamos el token recibido
                    val token = response.data.token
                    _token.value = token

                    // 2. Obtenemos el rol real del usuario usando el nuevo endpoint
                    val roleResponse = api.getUserRole("Bearer $token")

                    if (roleResponse.success && roleResponse.data != null) {
                        _role.value = roleResponse.data
                    } else {
                        _error.value = "No se pudo obtener el rol del usuario"
                    }

                } else {
                    // Error del backend (credenciales incorrectas, etc.)
                    _error.value = response.message
                }

            } catch (e: Exception) {
                // Errores reales: red, SSL, JSON, etc.
                e.printStackTrace()
                _error.value = "Error real: ${e.message}"
            } finally {
                // Quitamos el spinner
                _loading.value = false
            }
        }
    }

    /**
     * Permite a la UI establecer errores manualmente.
     */
    fun setError(msg: String) {
        _error.value = msg
    }
}







