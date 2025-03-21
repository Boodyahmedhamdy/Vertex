package com.iti.vertex.data.sources.remote.forecast

import com.iti.vertex.data.dtos.FullForecastResponse

interface IForecastRemoteDataSource {
    suspend fun getFullForecast(lat: Double, long: Double, ): FullForecastResponse
}