package com.iti.vertex.data.sources.remote.api

import com.iti.vertex.data.dtos.FullForecastResponse
import com.iti.vertex.data.dtos.current.CurrentWeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {


    @GET("forecast")
    suspend fun getFullForecast(
        @Query("lat") lat: Double,
        @Query("lon") long: Double,
    ): FullForecastResponse

    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") long: Double,
    ): CurrentWeatherResponse


    companion object {
        const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    }

}