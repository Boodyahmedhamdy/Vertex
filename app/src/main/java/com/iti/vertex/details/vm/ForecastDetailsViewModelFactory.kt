package com.iti.vertex.details.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.iti.vertex.data.repos.forecast.ForecastRepository
import com.iti.vertex.data.repos.settings.SettingsRepository

@Suppress("UNCHECKED_CAST")
class ForecastDetailsViewModelFactory(
    private val repository: ForecastRepository,
    private val settingsRepository: SettingsRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ForecastDetailsViewModel(repository, settingsRepository) as T
    }
}