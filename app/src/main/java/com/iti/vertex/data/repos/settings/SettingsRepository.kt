package com.iti.vertex.data.repos.settings

import com.iti.vertex.data.sources.local.settings.ISettingsLocalDataSource
import com.iti.vertex.data.sources.local.settings.WindSpeedUnit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

private const val TAG = "SettingsRepository"
class SettingsRepository(
    private val localDataSource: ISettingsLocalDataSource
) : ISettingsRepository {

    override fun getCurrentWindSpeedUnit() = localDataSource.getCurrentWindSpeed()
        .map { WindSpeedUnit.valueOf(it) }
        .flowOn(Dispatchers.IO)

    override suspend fun setWindSpeedUnit(windSpeedUnit: WindSpeedUnit) = localDataSource.setWindSpeedUnit(windSpeedUnit)


}