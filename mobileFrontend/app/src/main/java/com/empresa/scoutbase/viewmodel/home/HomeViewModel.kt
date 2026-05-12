package com.empresa.scoutbase.viewmodel.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empresa.scoutbase.data.remote.repository.UserTeamRepository
import com.empresa.scoutbase.data.remote.repository.UserTeamRepositoryImpl
import com.empresa.scoutbase.model.UserTeam.UserTeam
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: UserTeamRepository = UserTeamRepositoryImpl()
) : ViewModel() {

    private val _teams = MutableStateFlow<List<UserTeam>>(emptyList())
    val teams: StateFlow<List<UserTeam>> = _teams

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadTeams(token: String, userClubId: String) {
        _loading.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                val result = repository.getUserTeams(token, userClubId)
                _teams.value = result
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }
}




