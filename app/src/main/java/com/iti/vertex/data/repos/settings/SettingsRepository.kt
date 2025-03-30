package com.iti.vertex.data.repos.settings

import android.util.Log
import com.iti.vertex.data.sources.local.settings.ISettingsLocalDataSource
import com.iti.vertex.data.sources.local.settings.Language
import com.iti.vertex.data.sources.local.settings.LocationProvider
import com.iti.vertex.data.sources.local.settings.MyLocation
import com.iti.vertex.data.sources.local.settings.TempUnit
import com.iti.vertex.data.sources.local.settings.WindSpeedUnit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

private const val TAG = "SettingsRepository"
class SettingsRepository private constructor(
    private val localDataSource: ISettingsLocalDataSource
) : ISettingsRepository {

    // singleton
    companion object {
        private var INSTANCE: SettingsRepository? = null
        fun getInstance(settingsLocalDataSource: ISettingsLocalDataSource): SettingsRepository {
            return INSTANCE ?: SettingsRepository(settingsLocalDataSource)
        }
    }


    // wind speed
    override fun getCurrentWindSpeedUnit() = localDataSource.getCurrentWindSpeed()
        .map { WindSpeedUnit.valueOf(it) }.flowOn(Dispatchers.IO)
    override suspend fun setWindSpeedUnit(windSpeedUnit: WindSpeedUnit) = localDataSource.setWindSpeedUnit(windSpeedUnit)


    // language
    override fun getCurrentLanguage(): Flow<Language> = localDataSource.getCurrentLanguage()
        .map { Language.valueOf(it) }.flowOn(Dispatchers.IO)
    override suspend fun setCurrentLanguage(language: Language) {
        localDataSource.setCurrentLanguage(language)
    }


    // temp unit
    override fun getCurrentTempUnit(): Flow<TempUnit> = localDataSource.getCurrentTempUnit()
        .map { TempUnit.valueOf(it) }.flowOn(Dispatchers.IO)
    override suspend fun setCurrentTempUnit(tempUnit: TempUnit) = localDataSource.setCurrentTempUnit(tempUnit)


    // locationState provider
    override fun getCurrentLocationProvider(): Flow<LocationProvider> = localDataSource.getCurrentLocationProvider()
        .map { LocationProvider.valueOf(it) }.flowOn(Dispatchers.IO)
    override suspend fun setCurrentLocationProvider(locationProvider: LocationProvider) = localDataSource.setCurrentLocationProvider(locationProvider)


    // locationState
    override fun getCurrentLocation(): Flow<MyLocation> = localDataSource.getCurrentLocation()
    override suspend fun setCurrentLocation(lat: Double, long: Double) = localDataSource.setCurrentLocation(lat, long)
    override suspend fun setCurrentLocation(location: MyLocation) = localDataSource.setCurrentLocation(location)


}