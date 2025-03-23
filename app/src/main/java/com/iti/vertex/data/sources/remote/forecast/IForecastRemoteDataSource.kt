package com.iti.vertex.data.sources.remote.forecast

import com.iti.vertex.data.dtos.FullForecastResponse
import com.iti.vertex.data.dtos.current.CurrentWeatherResponse

interface IForecastRemoteDataSource {
    suspend fun getFullForecast(lat: Double, long: Double, ): FullForecastResponse
    suspend fun getCurrentWeather(lat: Double, long: Double): CurrentWeatherResponse
}