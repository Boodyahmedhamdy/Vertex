package com.iti.vertex.data.sources.local.forecast

import com.iti.vertex.data.sources.local.db.ForecastDao
import com.iti.vertex.data.sources.local.db.entities.ForecastEntity
import com.iti.vertex.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class ForecastLocalDataSource(
    private val dao: ForecastDao
) : IForecastLocalDataSource {

    override suspend fun insertForecast(forecastEntity: ForecastEntity) = withContext(Dispatchers.IO) {
        dao.insertForecast(forecastEntity)
    }

    override fun getAllForecast() = dao.getAllForecast()

    /*suspend fun getForecastByLatLong(lat: Double, long: Double): Result<out ForecastEntity> {
        return withContext(Dispatchers.IO) {
            try {
                val entity = dao.getForecastByLatLong(lat = lat, long = long)
                Result.Success(entity)
            } catch (ex: Exception) {
                Result.Error(ex.messageSharedFlow ?: "Error while Getting Forcast")
            }
        }
    }*/

    override suspend fun getForecastByLatLong(lat: Double, long: Double): ForecastEntity = withContext(Dispatchers.IO) {
        dao.getForecastByLatLong(lat = lat, lon = long)
    }

    override suspend fun deleteForecast(entity: ForecastEntity) {
        dao.deleteForecast(entity)
    }

    override suspend fun deleteForecastWithResult(entity: ForecastEntity): Result<out Unit> {
        return try {
            dao.deleteForecast(entity)
            Result.Success(Unit)
        } catch (ex: Exception) {
            Result.Error(ex.message ?: "error")
        }
    }

}