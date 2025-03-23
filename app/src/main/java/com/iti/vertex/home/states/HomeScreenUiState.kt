package com.iti.vertex.home.states

import com.iti.vertex.data.dtos.FullForecastResponse

data class HomeScreenUiState(
    val forecastUiState: ForecastUiState = ForecastUiState(),
    val currentWeatherUiState: CurrentWeatherUiState = CurrentWeatherUiState(),
    val isRefreshing: Boolean = false,
    val isLoading: Boolean = true
)
