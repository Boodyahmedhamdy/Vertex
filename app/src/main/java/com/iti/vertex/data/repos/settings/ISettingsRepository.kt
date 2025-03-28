package com.iti.vertex.data.repos.settings

import com.iti.vertex.data.sources.local.settings.Language
import com.iti.vertex.data.sources.local.settings.LocationProvider
import com.iti.vertex.data.sources.local.settings.TempUnit
import com.iti.vertex.data.sources.local.settings.WindSpeedUnit
import kotlinx.coroutines.flow.Flow

interface ISettingsRepository {
    fun getCurrentWindSpeedUnit(): Flow<WindSpeedUnit>
    suspend fun setWindSpeedUnit(windSpeedUnit: WindSpeedUnit)

    fun getCurrentLanguage(): Flow<Language>
    suspend fun setCurrentLanguage(language: Language)

    fun getCurrentTempUnit(): Flow<TempUnit>
    suspend fun setCurrentTempUnit(tempUnit: TempUnit)

    fun getCurrentLocationProvider(): Flow<LocationProvider>
    suspend fun setCurrentLocationProvider(locationProvider: LocationProvider)
}