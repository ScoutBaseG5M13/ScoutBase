package com.empresa.scoutbase.viewmodel.stats

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empresa.scoutbase.model.stats.StatDefinition
import com.empresa.scoutbase.repository.StatsRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CreateReportViewModel(
    private val repository: StatsRepository
) : ViewModel() {

    private val _stats = MutableStateFlow<List<StatDefinition>>(emptyList())
    val stats: StateFlow<List<StatDefinition>> = _stats

    // Guarda el valor seleccionat per cada stat (0–5)
    private val _values = MutableStateFlow<Map<String, Int>>(emptyMap())
    val values: StateFlow<Map<String, Int>> = _values

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _saved = MutableStateFlow(false)
    val saved: StateFlow<Boolean> = _saved

    fun loadStats(token: String) {
        viewModelScope.launch {
            val list = repository.getAllStats(token)
            _stats.value = list

            // Inicialitzar totes les stats a 0
            _values.value = list.associate { it.code to 0 }
        }
    }

    fun updateValue(code: String, value: Int) {
        _values.value = _values.value.toMutableMap().apply {
            put(code, value)
        }
    }

    fun saveReport(token: String, playerId: String) {
        viewModelScope.launch {
            _loading.value = true

            // 🔥 AQUI LOGUEJEM EXACTAMENT EL QUE S’ENVIA
            Log.d("REPORT_DEBUG", "Saving report for player $playerId")
            Log.d("REPORT_DEBUG", "Values sent: ${values.value}")

            try {
                values.value.forEach { (code, value) ->
                    Log.d("REPORT_DEBUG", "Sending -> code=$code value=$value")
                    repository.createPlayerStat(token, playerId, code, value)
                }

                _saved.value = true

            } catch (e: Exception) {
                Log.e("REPORT_ERROR", "Error saving report", e)
            }

            _loading.value = false
        }
    }
}



