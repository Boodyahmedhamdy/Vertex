package com.iti.vertex.data.repos.forecast

import com.iti.vertex.data.dtos.FullForecastResponse
import com.iti.vertex.data.dtos.current.CurrentWeatherResponse
import com.iti.vertex.data.sources.local.db.entities.ForecastEntity
import com.iti.vertex.data.sources.local.forecast.ForecastLocalDataSource
import com.iti.vertex.data.sources.local.forecast.IForecastLocalDataSource
import com.iti.vertex.data.sources.remote.forecast.IForecastRemoteDataSource
import com.iti.vertex.home.toWeatherIconUrl

class ForecastRepository(
    private val remoteDataSource: IForecastRemoteDataSource,
    private val localDataSource: IForecastLocalDataSource
) : IForecastRepository {

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

    /*suspend fun getForecastFlow(
        lat: Double, long: Double
    ): Flow<Result<FullForecastResponse>> = flow {

        emit(Result.Loading)

        try {
            val data = remoteDataSource.getFullForecast(lat = lat, long = long)
            emit(Result.Success(data = data))
        } catch (ex: Exception) {
            emit(Result.Error(ex.message))
        }
    }
*/

}