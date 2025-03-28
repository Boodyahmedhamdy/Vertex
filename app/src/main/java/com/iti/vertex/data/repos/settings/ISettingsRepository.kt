package com.iti.vertex.data.repos.settings

import com.iti.vertex.data.sources.local.settings.WindSpeedUnit
import kotlinx.coroutines.flow.Flow

interface ISettingsRepository {
    fun getCurrentWindSpeedUnit(): Flow<WindSpeedUnit>

    suspend fun setWindSpeedUnit(windSpeedUnit: WindSpeedUnit)
}