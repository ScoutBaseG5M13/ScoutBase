package com.empresa.scoutbase.viewmodel.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empresa.scoutbase.data.remote.repository.TeamRepository
import com.empresa.scoutbase.data.remote.repository.TeamRepositoryImpl
import com.empresa.scoutbase.model.team.Team
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val teamRepository: TeamRepository = TeamRepositoryImpl()
) : ViewModel() {

    private val _teams = MutableStateFlow<List<Team>>(emptyList())
    val teams: StateFlow<List<Team>> = _teams

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    /**
     * Ara només cal el token.
     * El backend ja sap quin usuari és a partir del token.
     */
    fun loadTeams(token: String) {
        _loading.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                val result = teamRepository.getTeams(token)
                _teams.value = result
            } catch (e: Exception) {
                e.printStackTrace()
                _error.value = e.message ?: "Error desconegut"
            } finally {
                _loading.value = false
            }
        }
    }
}


