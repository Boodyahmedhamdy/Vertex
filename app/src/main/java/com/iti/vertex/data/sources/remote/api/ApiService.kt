package com.iti.vertex.data.sources.remote.api

import com.iti.vertex.data.dtos.FullForecastResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {


    @GET("forecast")
    suspend fun getFullForecast(
        @Query("lat") lat: Double,
        @Query("lon") long: Double,
        @Query("units") units: String = "metric",
        @Query("appid") apiKey: String = API_KEY
    ): FullForecastResponse


    companion object {
        private const val API_KEY = "81e1b2e388ab26fa596925b472ee82ae"
        const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    }

}