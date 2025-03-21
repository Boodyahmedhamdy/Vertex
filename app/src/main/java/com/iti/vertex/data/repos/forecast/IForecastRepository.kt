package com.iti.vertex.data.repos.forecast

import com.iti.vertex.data.dtos.FullForecastResponse

interface IForecastRepository {
    suspend fun getForecast(lat: Double, long: Double): FullForecastResponse
}