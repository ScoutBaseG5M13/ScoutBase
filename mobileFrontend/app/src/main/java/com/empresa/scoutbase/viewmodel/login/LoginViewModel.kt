package com.empresa.scoutbase.viewmodel.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empresa.scoutbase.data.remote.ApiService
import com.empresa.scoutbase.model.login.LoginRequest
import com.empresa.scoutbase.model.login.LoginResponse
import com.empresa.scoutbase.model.login.ApiResponse
import com.empresa.scoutbase.model.userclub.UserClubResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val api = ApiService.loginApi

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _token = MutableStateFlow<String?>(null)
    val token: StateFlow<String?> = _token

    private val _role = MutableStateFlow<String?>(null)
    val role: StateFlow<String?> = _role

    private val _userClubId = MutableStateFlow<String?>(null)
    val userClubId: StateFlow<String?> = _userClubId

    fun login(username: String, password: String) {
        _loading.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                // 1) LOGIN → OBTENER TOKEN
                val loginResponse = api.login(LoginRequest(username, password))

                if (!loginResponse.success || loginResponse.data == null) {
                    _error.value = loginResponse.message
                    _loading.value = false
                    return@launch
                }

                val token = loginResponse.data.token
                _token.value = token

                // 2) OBTENER CLUBS DEL USUARIO
                val clubsResponse = api.getUserClubs("Bearer $token")

                if (!clubsResponse.success || clubsResponse.data.isNullOrEmpty()) {
                    _error.value = "No se encontraron clubes para este usuario"
                    _loading.value = false
                    return@launch
                }

                val clubId = clubsResponse.data[0].id
                _userClubId.value = clubId

                // 3) OBTENER ROL REAL DENTRO DEL CLUB
                val roleResponse = api.getUserRoleInsideClub(
                    token = "Bearer $token",
                    userClubId = clubId
                )

                if (!roleResponse.success || roleResponse.data == null) {
                    _error.value = "No se pudo obtener el rol del usuario"
                    _loading.value = false
                    return@launch
                }

                _role.value = roleResponse.data

            } catch (e: Exception) {
                e.printStackTrace()
                _error.value = "Error real: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    fun setError(msg: String) {
        _error.value = msg
    }

    fun logout() {
        _token.value = null
        _role.value = null
        _userClubId.value = null
        _error.value = null
    }
}















