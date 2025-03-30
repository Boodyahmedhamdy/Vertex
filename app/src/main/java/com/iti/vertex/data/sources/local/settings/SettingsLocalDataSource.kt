package com.iti.vertex.data.sources.local.settings

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

private const val TAG = "SettingsLocalDataSource"
class SettingsLocalDataSource(
    private val dataStoreHelper: DataStoreHelper
) : ISettingsLocalDataSource {

    // wind speed
    override fun getCurrentWindSpeed() = dataStoreHelper.getCurrentWindSpeed()
    override suspend fun setWindSpeedUnit(windSpeedUnit: WindSpeedUnit) = withContext(Dispatchers.IO) {
        dataStoreHelper.setCurrentWindSpeedUnit(windSpeedUnit)
    }

    // language
    override fun getCurrentLanguage(): Flow<String> = dataStoreHelper.getCurrentLanguage()
    override suspend fun setCurrentLanguage(language: Language) = withContext(Dispatchers.IO) {
        Log.i(TAG, "setCurrentLanguage: by $language")
        dataStoreHelper.setCurrentLanguage(language)

    }

    // locationState provider
    override fun getCurrentLocationProvider(): Flow<String> = dataStoreHelper.getCurrentLocationProvider()
    override suspend fun setCurrentLocationProvider(locationProvider: LocationProvider) = withContext(Dispatchers.IO) {
        dataStoreHelper.setCurrentLocationProvider(locationProvider)
    }

    // temp unit
    override fun getCurrentTempUnit(): Flow<String> = dataStoreHelper.getCurrentTempUnit()
    override suspend fun setCurrentTempUnit(tempUnit: TempUnit) = withContext(Dispatchers.IO) {
        dataStoreHelper.setCurrentTempUnit(tempUnit)
    }

    // locationState
    override fun getCurrentLocation(): Flow<MyLocation> = dataStoreHelper.getCurrentLocation()
    override suspend fun setCurrentLocation(lat: Double, long: Double) = withContext(Dispatchers.IO) {
        dataStoreHelper.setCurrentLocation(lat, long)
    }
    override suspend fun setCurrentLocation(location: MyLocation) = withContext(Dispatchers.IO) {
        dataStoreHelper.setCurrentLocation(location)
    }


}