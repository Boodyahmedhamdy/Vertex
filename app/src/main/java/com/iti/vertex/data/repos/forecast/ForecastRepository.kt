package com.iti.vertex.data.repos.forecast

import com.iti.vertex.data.dtos.FullForecastResponse
import com.iti.vertex.data.dtos.current.CurrentWeatherResponse
import com.iti.vertex.data.sources.local.db.entities.ForecastEntity
import com.iti.vertex.data.sources.local.forecast.IForecastLocalDataSource
import com.iti.vertex.data.sources.remote.forecast.IForecastRemoteDataSource

class ForecastRepository private constructor(
    private val remoteDataSource: IForecastRemoteDataSource,
    private val localDataSource: IForecastLocalDataSource
) : IForecastRepository {

    companion object {
        private var INSTANCE: ForecastRepository? = null
        fun getInstance(remoteDataSource: IForecastRemoteDataSource, localDataSource: IForecastLocalDataSource): ForecastRepository {
            return INSTANCE ?: ForecastRepository(remoteDataSource, localDataSource)
        }
    }

    override suspend fun getForecast(lat: Double, long: Double): FullForecastResponse {
        try {
            val data = remoteDataSource.getFullForecast(lat, long)
            return data
        } catch (ex: Exception) {
            return FullForecastResponse()
        }
    }

    override suspend fun getCurrentWeather(lat: Double, long: Double): CurrentWeatherResponse {
        try {
            val data = remoteDataSource.getCurrentWeather(lat = lat, long = long)
            return data
        } catch (ex: Exception) {
            return CurrentWeatherResponse()
        }
    }

    fun getFavoriteForecasts() = localDataSource.getAllForecast()

    suspend fun addToFavorite(forecastEntity: ForecastEntity) {
        localDataSource.insertForecast(forecastEntity)
    }

    override suspend fun getFavoriteForecastByLatLong(lat: Double, long: Double): ForecastEntity {
        return localDataSource.getForecastByLatLong(lat, long)
    }

    override suspend fun deleteForecast(entity: ForecastEntity) {
        localDataSource.deleteForecast(entity)
    }

}