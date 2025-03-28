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
import kotlin.math.log

private const val TAG = "DataStoreHelper"

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "VERTEX_SETTINGS")

class DataStoreHelper(private val context: Context) {

    companion object {
        val windSpeedUnitKey = stringPreferencesKey("WIND_SPEED_UNIT_KEY")
        val tempUnitKey = stringPreferencesKey("TEMP_UNIT_KEY")
        val languageKey = stringPreferencesKey("LANGUAGE_KEY")
        val locationProviderKey = stringPreferencesKey("LOCATION_PROVIDER_KEY")
    }

    // temp
    fun getCurrentTempUnit(): Flow<String> = context.dataStore.data.map { settings ->
        settings[tempUnitKey] ?: TempUnit.KELVIN.name
    }
    suspend fun setCurrentTempUnit(tempUnit: TempUnit) {
        context.dataStore.edit { settings ->
            settings[tempUnitKey] = tempUnit.name
        }
    }

    // wind speed
    fun getCurrentWindSpeed(): Flow<String> = context.dataStore.data.map {settings ->
        settings[windSpeedUnitKey] ?: WindSpeedUnit.MILE_PER_HOUR.name
    }
    suspend fun setCurrentWindSpeedUnit(windSpeedUnit: WindSpeedUnit) {
        context.dataStore.edit {settings ->
            settings[windSpeedUnitKey] = windSpeedUnit.name
        }
    }

    // language
    suspend fun setCurrentLanguage(language: Language) {
        context.dataStore.edit { settings ->
            settings[languageKey] = language.name
            Log.i(TAG, "setCurrentLanguage: by $language")
        }
    }
    fun getCurrentLanguage(): Flow<String> = context.dataStore.data.map { settings ->
        settings[languageKey] ?: Language.DEVICE_DEFAULT.name
    }

    // location provider
    suspend fun setCurrentLocationProvider(locationProvider: LocationProvider) {
        context.dataStore.edit { settings ->
            settings[locationProviderKey] = locationProvider.name
        }
    }
    fun getCurrentLocationProvider(): Flow<String> = context.dataStore.data.map { settings ->
        settings[locationProviderKey] ?: LocationProvider.GPS.name
    }

}


/**
 * @property GPS is the default
 * */
enum class LocationProvider(
    @StringRes val displayName: Int = R.string.settings
) { GPS(R.string.gps), MAP(R.string.map) }

/**
 * @property DEVICE_DEFAULT is the default and depends on device language
 * */
enum class Language(
    @StringRes val displayName: Int = R.string.settings
) {
    ARABIC(R.string.arabic),
    ENGLISH(R.string.english),
    DEVICE_DEFAULT(R.string.device_default)
}

/**
 * @param converter is the lambda to convert from default value to other values
 * @property KELVIN is the default coming from the api
 * */
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