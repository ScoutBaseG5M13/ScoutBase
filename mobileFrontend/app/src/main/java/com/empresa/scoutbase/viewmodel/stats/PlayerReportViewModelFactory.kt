package com.empresa.scoutbase.viewmodel.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.empresa.scoutbase.repository.StatsRepository

class PlayerReportViewModelFactory(
    private val repository: StatsRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlayerReportViewModel::class.java)) {
            return PlayerReportViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


