package com.iti.vertex.details.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.iti.vertex.data.repos.forecast.ForecastRepository

class ForecastDetailsViewModelFactory(
    private val repository: ForecastRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ForecastDetailsViewModel(repository) as T
    }
}