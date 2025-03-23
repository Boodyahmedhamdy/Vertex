package com.iti.vertex.data.repos.forecast

import com.iti.vertex.data.dtos.FullForecastResponse
import com.iti.vertex.data.dtos.current.CurrentWeatherResponse

interface IForecastRepository {
    suspend fun getForecast(lat: Double, long: Double): FullForecastResponse
    suspend fun getCurrentWeather(lat: Double, long: Double): CurrentWeatherResponse
}