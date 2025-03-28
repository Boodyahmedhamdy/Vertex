package com.iti.vertex.data.sources.local.settings

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

private const val TAG = "SettingsLocalDataSource"
class SettingsLocalDataSource(
    private val dataStoreHelper: DataStoreHelper
) : ISettingsLocalDataSource {

    override fun getCurrentWindSpeed() = dataStoreHelper.getCurrentWindSpeed()
    override suspend fun setWindSpeedUnit(windSpeedUnit: WindSpeedUnit) = withContext(Dispatchers.IO) {
        dataStoreHelper.setCurrentWindSpeedUnit(windSpeedUnit)
    }

    override fun getCurrentLanguage(): Flow<String> = dataStoreHelper.getCurrentLanguage()
    override suspend fun setCurrentLanguage(language: Language) = withContext(Dispatchers.IO) {
        Log.i(TAG, "setCurrentLanguage: by $language")
        dataStoreHelper.setCurrentLanguage(language)

    }

    override fun getCurrentLocationProvider(): Flow<String> = dataStoreHelper.getCurrentLocationProvider()
    override suspend fun setCurrentLocationProvider(locationProvider: LocationProvider) = withContext(Dispatchers.IO) {
        dataStoreHelper.setCurrentLocationProvider(locationProvider)
    }

    override fun getCurrentTempUnit(): Flow<String> = dataStoreHelper.getCurrentTempUnit()
    override suspend fun setCurrentTempUnit(tempUnit: TempUnit) = withContext(Dispatchers.IO) {
        dataStoreHelper.setCurrentTempUnit(tempUnit)
    }


}