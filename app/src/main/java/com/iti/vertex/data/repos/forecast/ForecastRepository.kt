package com.iti.vertex.data.repos.forecast

import com.iti.vertex.data.dtos.FullForecastResponse
import com.iti.vertex.data.sources.remote.forecast.IForecastRemoteDataSource

class ForecastRepository(
    private val remoteDataSource: IForecastRemoteDataSource,
) : IForecastRepository {

    override suspend fun getForecast(lat: Double, long: Double): FullForecastResponse {
        try {
            val data = remoteDataSource.getFullForecast(lat, long)
            return data
        } catch (ex: Exception) {
            return FullForecastResponse()
        }
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