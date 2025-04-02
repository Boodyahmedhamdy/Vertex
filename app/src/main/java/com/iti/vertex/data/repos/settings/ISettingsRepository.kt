package com.iti.vertex.data.repos.settings

import com.iti.vertex.data.sources.local.settings.Language
import com.iti.vertex.data.sources.local.settings.LocationProvider
import com.iti.vertex.data.sources.local.settings.MyLocation
import com.iti.vertex.data.sources.local.settings.TempUnit
import com.iti.vertex.data.sources.local.settings.WindSpeedUnit
import kotlinx.coroutines.flow.Flow

interface ISettingsRepository {
    // wind speed
    fun getCurrentWindSpeedUnit(): Flow<WindSpeedUnit>
    suspend fun setWindSpeedUnit(windSpeedUnit: WindSpeedUnit)

    // language
    fun getCurrentLanguage(): Flow<Language>
    suspend fun setCurrentLanguage(language: Language)

    // temp unit
    fun getCurrentTempUnit(): Flow<TempUnit>
    suspend fun setCurrentTempUnit(tempUnit: TempUnit)

    // locationState provider
    fun getCurrentLocationProvider(): Flow<LocationProvider>
    suspend fun setCurrentLocationProvider(locationProvider: LocationProvider)

    // locationState
    fun getCurrentLocation(): Flow<MyLocation>
    suspend fun setCurrentLocation(location: MyLocation)
}