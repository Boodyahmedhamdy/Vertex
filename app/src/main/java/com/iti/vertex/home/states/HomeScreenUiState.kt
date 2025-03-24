package com.iti.vertex.home.states

import com.iti.vertex.data.dtos.SimpleForecastItem
import com.iti.vertex.data.dtos.Weather

data class HomeScreenUiState(
    val forecastUiState: ForecastUiState = ForecastUiState(),
    val currentWeatherUiState: CurrentWeatherUiState = CurrentWeatherUiState(),
    val forecastMap: Map<String, List<SimpleForecastItem>> = mapOf("" to listOf(SimpleForecastItem(weather = listOf(Weather())))),
    val isRefreshing: Boolean = false,
    val isLoading: Boolean = true,
    val lat: Double = 0.0, val long: Double = 0.0
)
