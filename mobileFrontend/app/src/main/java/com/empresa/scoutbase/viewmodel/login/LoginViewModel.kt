package com.empresa.scoutbase.viewmodel.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empresa.scoutbase.data.remote.ApiService
import com.empresa.scoutbase.data.remote.mock.LoginApiMock
import com.empresa.scoutbase.model.login.LoginRequest
import com.empresa.scoutbase.model.login.LoginResponse
import com.empresa.scoutbase.model.login.ApiResponse
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

    fun login(username: String, password: String) {
        _loading.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                // 1) LOGIN
                val request = LoginRequest(username, password)
                val response: ApiResponse<LoginResponse> = api.login(request)

                if (response.success && response.data != null) {
                    val token = response.data.token
                    _token.value = token

                    // 2) MOCK FORZADO DEL ROL
                    val roleResponse = LoginApiMock.getUserRoleMock()

                    _role.value = roleResponse.data

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

    fun setError(msg: String) {
        _error.value = msg
    }

    fun logout() {
        _token.value = null
        _role.value = null
        _error.value = null
    }

    fun setFakeAuth(token: String, role: String) {
        _token.value = token
        _role.value = role
    }
}











