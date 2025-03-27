package com.iti.vertex.data.sources.local.forecast

import com.iti.vertex.data.sources.local.db.entities.ForecastEntity
import com.iti.vertex.utils.Result
import kotlinx.coroutines.flow.Flow

interface IForecastLocalDataSource {
    suspend fun insertForecast(forecastEntity: ForecastEntity)
    fun getAllForecast(): Flow<List<ForecastEntity>>
    suspend fun getForecastByLatLong(lat: Double, long: Double): ForecastEntity
    suspend fun deleteForecast(entity: ForecastEntity)
}