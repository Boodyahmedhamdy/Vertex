package com.iti.vertex.data.repos.forecast

import com.iti.vertex.data.dtos.FullForecastResponse
import com.iti.vertex.data.dtos.current.CurrentWeatherResponse
import com.iti.vertex.data.sources.local.db.entities.ForecastEntity
import kotlinx.coroutines.flow.Flow

interface IForecastRepository {
    suspend fun getForecast(lat: Double, long: Double): FullForecastResponse
    suspend fun getCurrentWeather(lat: Double, long: Double): CurrentWeatherResponse
    suspend fun getFavoriteForecastByLatLong(lat: Double, long: Double): ForecastEntity
    suspend fun deleteForecast(entity: ForecastEntity)
    fun getFavoriteForecasts(): Flow<List<ForecastEntity>>
}