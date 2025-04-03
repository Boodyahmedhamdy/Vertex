package com.iti.vertex.data.sources.local.settings

import android.content.Context
import android.location.Geocoder
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.iti.vertex.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

private const val TAG = "DataStoreHelper"

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "VERTEX_SETTINGS")

data class MyLocation(val lat: Double, val long: Double, val cityName: String = "")

class DataStoreHelper(private val context: Context) {

    companion object {
        val windSpeedUnitKey = stringPreferencesKey("WIND_SPEED_UNIT_KEY")
        val tempUnitKey = stringPreferencesKey("TEMP_UNIT_KEY")
        val languageKey = stringPreferencesKey("LANGUAGE_KEY")
        val locationProviderKey = stringPreferencesKey("LOCATION_PROVIDER_KEY")
        val latitudeKey = doublePreferencesKey("LATITUDE_KEY")
        val longitudeKey = doublePreferencesKey("LONGITUDE_KEY")
        val cityNameKey = stringPreferencesKey("CITY_NAME_KEY")
    }

    // locationState
    fun getCurrentLocation() =
        context.dataStore.data.map { settings ->
            MyLocation(
                lat = settings[latitudeKey] ?: 0.0,
                long = settings[longitudeKey] ?: 0.0,
                cityName = settings[cityNameKey] ?: "NONE"
            )
        }

    suspend fun setCurrentLocation(location: MyLocation) {
        val cityName = try {
            val geoCoder = Geocoder(context)
            val names = geoCoder.getFromLocation(location.lat, location.long, 1) ?: emptyList()
            names.first().countryName
        } catch (ex: Exception) { "NONE" }

        context.dataStore.edit { settings ->
            settings[latitudeKey] = location.lat
            settings[longitudeKey] = location.long
            settings[cityNameKey] = cityName
        }
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
            withContext(Dispatchers.Main) {
                val appLocale = LocaleListCompat.forLanguageTags(language.localeCode)
                AppCompatDelegate.setApplicationLocales(appLocale)
            }
        }
    }
    fun getCurrentLanguage(): Flow<String> = context.dataStore.data.map { settings ->
        settings[languageKey] ?: Language.DEVICE_DEFAULT.name
    }

    // locationState provider
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
    @StringRes val displayName: Int = R.string.settings,
    val localeCode: String = "en-US"
) {
    ARABIC(R.string.arabic, localeCode = "ar"),
    ENGLISH(R.string.english, localeCode = "en"),
    DEVICE_DEFAULT(R.string.device_default, localeCode = "en-US")
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