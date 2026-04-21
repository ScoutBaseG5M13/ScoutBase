package com.empresa.scoutbase.viewmodel.players

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empresa.scoutbase.data.remote.repository.PlayerRepository
import com.empresa.scoutbase.data.remote.repository.PlayerRepositoryImpl
import com.empresa.scoutbase.model.player.PlayerCreateRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlayerCreateViewModel(
    private val repository: PlayerRepository = PlayerRepositoryImpl()
) : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _success = MutableStateFlow(false)
    val success: StateFlow<Boolean> = _success

    fun createPlayer(token: String, teamId: String, request: PlayerCreateRequest) {
        _loading.value = true
        _error.value = null
        _success.value = false

        viewModelScope.launch {
            try {
                repository.createPlayer(token, teamId, request)
                _success.value = true
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }
}


