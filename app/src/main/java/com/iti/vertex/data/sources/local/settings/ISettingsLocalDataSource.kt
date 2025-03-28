package com.iti.vertex.data.sources.local.settings

import kotlinx.coroutines.flow.Flow

interface ISettingsLocalDataSource {
    fun getCurrentWindSpeed(): Flow<String>

    suspend fun setWindSpeedUnit(windSpeedUnit: WindSpeedUnit)
}