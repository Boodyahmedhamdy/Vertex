package com.iti.vertex.data.sources.local.forecast

import com.iti.vertex.data.sources.local.db.ForecastDao
import com.iti.vertex.data.sources.local.db.entities.ForecastEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ForecastLocalDataSource(
    private val dao: ForecastDao
) : IForecastLocalDataSource {

    override suspend fun insertForecast(forecastEntity: ForecastEntity) = withContext(Dispatchers.IO) {
        dao.insertForecast(forecastEntity)
    }

    override fun getAllForecast() = dao.getAllForecast()

}