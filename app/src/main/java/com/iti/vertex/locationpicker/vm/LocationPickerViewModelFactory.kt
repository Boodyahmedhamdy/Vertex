package com.iti.vertex.locationpicker.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.iti.vertex.data.repos.forecast.ForecastRepository
import com.iti.vertex.data.repos.settings.SettingsRepository

@Suppress("UNCHECKED_CAST")
class LocationPickerViewModelFactory(
    private val forecastRepository: ForecastRepository,
    private val settingsRepository: SettingsRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LocationPickerViewModel(forecastRepo = forecastRepository, settingsRepo = settingsRepository) as T
    }
}