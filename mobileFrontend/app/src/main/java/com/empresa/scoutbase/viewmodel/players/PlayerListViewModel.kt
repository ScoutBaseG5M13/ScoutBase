package com.empresa.scoutbase.viewmodel.players

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empresa.scoutbase.data.remote.repository.PlayerRepository
import com.empresa.scoutbase.data.remote.repository.PlayerRepositoryImpl
import com.empresa.scoutbase.model.player.Player
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlayerListViewModel(
    private val repository: PlayerRepository = PlayerRepositoryImpl()
) : ViewModel() {

    private val _players = MutableStateFlow<List<Player>>(emptyList())
    val players: StateFlow<List<Player>> = _players

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadPlayers(token: String, teamId: String) {
        _loading.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                val result = repository.getPlayersByTeam(token, teamId)
                _players.value = result
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }
}
