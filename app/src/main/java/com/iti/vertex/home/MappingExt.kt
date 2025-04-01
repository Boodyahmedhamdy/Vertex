package com.iti.vertex.home

import com.iti.vertex.data.sources.local.db.entities.ForecastEntity
import com.iti.vertex.home.states.ForecastUiState

fun ForecastEntity.toUiState(): ForecastUiState {
    return ForecastUiState(city = city, list = list)
}