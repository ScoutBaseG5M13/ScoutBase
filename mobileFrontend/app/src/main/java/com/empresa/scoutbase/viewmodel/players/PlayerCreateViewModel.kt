// com/empresa/scoutbase/viewmodel/players/PlayerCreateViewModel.kt
package com.empresa.scoutbase.viewmodel.players

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empresa.scoutbase.model.player.PlayerCreateRequest
import com.empresa.scoutbase.repository.PlayerRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlayerCreateViewModel(
    private val repository: PlayerRepositoryImpl = PlayerRepositoryImpl()
) : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _success = MutableStateFlow(false)
    val success: StateFlow<Boolean> = _success

    fun createPlayer(token: String, teamId: String, req: PlayerCreateRequest) {
        _loading.value = true
        _error.value = null
        _success.value = false

        viewModelScope.launch {
            try {
                repository.createPlayer(token, teamId, req)
                _success.value = true
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }
}





