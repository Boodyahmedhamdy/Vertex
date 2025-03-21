package com.iti.vertex.data.sources.remote.api

import com.iti.vertex.BuildConfig
import com.iti.vertex.data.dtos.FullForecastResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {


    @GET("forecast")
    suspend fun getFullForecast(
        @Query("lat") lat: Double,
        @Query("lon") long: Double,
        @Query("units") units: String = "metric",
        /*@Query("appid") apiKey: String = BuildConfig.API_KEY*/
    ): FullForecastResponse


    companion object {
        const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    }

}