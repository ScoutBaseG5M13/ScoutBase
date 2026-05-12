package com.empresa.scoutbase.viewmodel.players

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empresa.scoutbase.data.remote.ApiService
import com.empresa.scoutbase.model.player.TeamData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SelectTeamViewModel : ViewModel() {

    private val api = ApiService.playerApi

    private val _teams = MutableStateFlow<List<TeamData>>(emptyList())
    val teams: StateFlow<List<TeamData>> = _teams

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadTeams(token: String, clubId: String) {
        _loading.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                val response = api.getTeamsFromClub("Bearer $token", clubId)
                _teams.value = response.data
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }
}


