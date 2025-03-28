package com.iti.vertex.data.sources.local.settings

import android.content.Context
import android.util.Log
import androidx.annotation.StringRes
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.iti.vertex.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val TAG = "DataStoreHelper"

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "VERTEX_SETTINGS")

class DataStoreHelper(private val context: Context) {

    companion object {
        val windSpeedUnitKey = stringPreferencesKey("WIND_SPEED_UNIT_KEY")
        val tempUnitKey = stringPreferencesKey("TEMP_UNIT_KEY")
    }

    fun getCurrentTempUnit(): Flow<String> = context.dataStore.data.map { settings ->
        settings[tempUnitKey] ?: TempUnit.KELVIN.name
    }

    suspend fun setTempUnit(tempUnit: TempUnit) = context.dataStore.edit {settings ->
        settings[tempUnitKey] = tempUnit.name
    }

    fun getCurrentWindSpeed(): Flow<String> = context.dataStore.data.map {settings ->
        Log.i(TAG, "getCurrentWindSpeed: ${settings[windSpeedUnitKey]}")
        settings[windSpeedUnitKey] ?: WindSpeedUnit.MILE_PER_HOUR.name
    }

    suspend fun setWindSpeedUnit(windSpeedUnit: WindSpeedUnit) {
        context.dataStore.edit {settings ->
            settings[windSpeedUnitKey] = windSpeedUnit.name
            Log.i(TAG, "setWindSpeedUnit: $windSpeedUnit")
        }
    }
}



enum class TempUnit(
    val converter: (Double) -> Double,
    @StringRes val displayName: Int = R.string.settings
) {
    KELVIN(converter = { it }, displayName = R.string.kelvin),
    FAHRENHEIT(converter = { k -> 1.8 * (k - 273.05) + 32 }, displayName = R.string.fahrenheit),
    Celsius(converter = { k -> k - 273.15 }, displayName = R.string.celisuis)
}


/**
 * @param converter is the lambda to convert from default value to other values
 * @property MILE_PER_HOUR is the default, so it returns the same value
 * */
enum class WindSpeedUnit(
    val converter: (Double) -> Double,
    @StringRes val displayName: Int = R.string.settings
) {
    MILE_PER_HOUR(converter = { it }, displayName = R.string.mile_per_hour), // default value
    METER_PER_SECOND(converter = { it * 0.44704 }, displayName = R.string.meter_per_second)
}