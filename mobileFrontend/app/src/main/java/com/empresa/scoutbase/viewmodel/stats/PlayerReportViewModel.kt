package com.empresa.scoutbase.viewmodel.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empresa.scoutbase.model.stats.PlayerStat
import com.empresa.scoutbase.repository.StatsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlayerReportViewModel(
    private val repository: StatsRepository
) : ViewModel() {

    private val _stats = MutableStateFlow<List<PlayerStat>>(emptyList())
    val stats: StateFlow<List<PlayerStat>> = _stats

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    fun loadPlayerStats(token: String, playerId: String) {
        viewModelScope.launch {
            _loading.value = true
            _stats.value = repository.getPlayerStats(token, playerId)
            _loading.value = false
        }
    }

    // Mitjana global
    fun globalAverage(): Double =
        if (_stats.value.isEmpty()) 0.0
        else _stats.value.map { it.value }.average()

    // Mitjana per grup
    fun averageByType(type: String): Double {
        val filtered = _stats.value.filter { it.code.startsWithType(type) }
        return if (filtered.isEmpty()) 0.0 else filtered.map { it.value }.average()
    }

    // Millors 2 stats per grup
    fun bestTwo(type: String): List<PlayerStat> =
        _stats.value
            .filter { it.code.startsWithType(type) }
            .sortedByDescending { it.value }
            .take(2)
}

// EXTENSIÓ PER RELACIONAR CODE AMB TIPUS
private fun String.startsWithType(type: String): Boolean {
    return when (type) {
        "OFENSIVO" -> listOf("C", "D", "T", "P", "R", "J", "U", "M").any { this.startsWith(it) }
        "DEFENSIVO" -> listOf("O", "T", "S", "C", "M", "I", "D", "J", "P", "R").any { this.startsWith(it) }
        "MENTAL" -> listOf("T", "P", "A", "C", "L", "S").any { this.startsWith(it) }
        "FISICO" -> listOf("C", "F", "V", "P", "S", "R").any { this.startsWith(it) }
        else -> false
    }
}


