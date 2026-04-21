package com.empresa.scoutbase.viewmodel.players

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empresa.scoutbase.data.remote.repository.PlayerRepository
import com.empresa.scoutbase.data.remote.repository.PlayerRepositoryImpl
import com.empresa.scoutbase.model.player.Player
import com.empresa.scoutbase.model.player.PlayerUpdateRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlayerEditViewModel(
    private val repository: PlayerRepository = PlayerRepositoryImpl()
) : ViewModel() {

    private val _player = MutableStateFlow<Player?>(null)
    val player: StateFlow<Player?> = _player

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _success = MutableStateFlow(false)
    val success: StateFlow<Boolean> = _success

    private val _deleted = MutableStateFlow(false)
    val deleted: StateFlow<Boolean> = _deleted

    fun setPlayerData(player: Player) {
        _player.value = player
    }

    fun updatePlayer(token: String, playerId: String, request: PlayerUpdateRequest) {
        _loading.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                repository.updatePlayer(token, playerId, request)
                _success.value = true
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun deletePlayer(token: String, playerId: String) {
        _loading.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                repository.deletePlayer(token, playerId)
                _deleted.value = true
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }
}



