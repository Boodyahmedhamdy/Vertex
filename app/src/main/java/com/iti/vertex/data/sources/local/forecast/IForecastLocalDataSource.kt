package com.iti.vertex.data.sources.local.forecast

import com.iti.vertex.data.sources.local.db.entities.ForecastEntity
import kotlinx.coroutines.flow.Flow

interface IForecastLocalDataSource {
    suspend fun insertForecast(forecastEntity: ForecastEntity)
    fun getAllForecast(): Flow<List<ForecastEntity>>
}