package com.iti.vertex.details.vm

import com.iti.vertex.data.dtos.SimpleForecastItem
import com.iti.vertex.data.dtos.Weather
import com.iti.vertex.data.sources.local.db.entities.ForecastEntity

data class ForecastDetailsScreenUiState(
    val isLoading: Boolean = false,
    val forecastEntity: ForecastEntity = ForecastEntity(
        list = listOf(SimpleForecastItem(weather = listOf(Weather())))
    )
)
