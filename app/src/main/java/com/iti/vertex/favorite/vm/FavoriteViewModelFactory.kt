package com.iti.vertex.favorite.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.iti.vertex.data.repos.forecast.ForecastRepository

@Suppress("UNCHECKED_CAST")
class FavoriteViewModelFactory(
    private val forecastRepository: ForecastRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FavoriteViewModel(forecastRepository) as T
    }
}