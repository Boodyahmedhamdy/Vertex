package com.iti.vertex.data.sources.remote.forecast

import com.iti.vertex.data.sources.remote.api.ApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext


class ForecastRemoteDataSource(
    private val api: ApiService,
    private val ioDispatcher: CoroutineDispatcher
): IForecastRemoteDataSource {
    override suspend fun getFullForecast(lat: Double, long: Double) = withContext(ioDispatcher) {
        api.getFullForecast(lat, long)
    }
}