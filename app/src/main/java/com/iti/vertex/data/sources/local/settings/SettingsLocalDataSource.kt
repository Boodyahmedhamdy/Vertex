package com.iti.vertex.data.sources.local.settings

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SettingsLocalDataSource(
    private val dataStoreHelper: DataStoreHelper
) : ISettingsLocalDataSource {

    override fun getCurrentWindSpeed() = dataStoreHelper.getCurrentWindSpeed()
    override suspend fun setWindSpeedUnit(windSpeedUnit: WindSpeedUnit) = withContext(Dispatchers.IO) {
        dataStoreHelper.setWindSpeedUnit(windSpeedUnit)
    }

}