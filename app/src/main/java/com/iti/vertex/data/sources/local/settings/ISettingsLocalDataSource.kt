package com.iti.vertex.data.sources.local.settings

import kotlinx.coroutines.flow.Flow

interface ISettingsLocalDataSource {
    fun getCurrentWindSpeed(): Flow<String>
    suspend fun setWindSpeedUnit(windSpeedUnit: WindSpeedUnit)

    fun getCurrentLanguage(): Flow<String>
    suspend fun setCurrentLanguage(language: Language)

    fun getCurrentLocationProvider(): Flow<String>
    suspend fun setCurrentLocationProvider(locationProvider: LocationProvider)

    fun getCurrentTempUnit(): Flow<String>
    suspend fun setCurrentTempUnit(tempUnit: TempUnit)

}