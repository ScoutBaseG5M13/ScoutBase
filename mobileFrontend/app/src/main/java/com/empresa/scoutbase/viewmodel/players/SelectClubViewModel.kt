package com.empresa.scoutbase.viewmodel.players

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empresa.scoutbase.data.remote.ApiService
import com.empresa.scoutbase.model.player.ClubData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SelectClubViewModel : ViewModel() {

    private val api = ApiService.playerApi

    private val _clubs = MutableStateFlow<List<ClubData>>(emptyList())
    val clubs: StateFlow<List<ClubData>> = _clubs

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadClubs(token: String, userClubId: String) {
        _loading.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                val response = api.getClubsFromUserClub("Bearer $token", userClubId)
                _clubs.value = response.data
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }
}


