package com.iti.vertex.home.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.vertex.R
import com.iti.vertex.data.dtos.current.CurrentWeatherResponse
import com.iti.vertex.data.repos.forecast.IForecastRepository
import com.iti.vertex.data.repos.settings.ISettingsRepository
import com.iti.vertex.data.sources.local.db.entities.ForecastEntity
import com.iti.vertex.data.sources.local.settings.MyLocation
import com.iti.vertex.data.sources.local.settings.WindSpeedUnit
import com.iti.vertex.utils.Result
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

private const val TAG = "HomeViewModel"

class HomeViewModel(
    private val repository: IForecastRepository,
    private val settingsRepo: ISettingsRepository
): ViewModel(){

    private val _currentWeatherState: MutableStateFlow<Result<out CurrentWeatherResponse>> = MutableStateFlow(Result.Loading)
    val currentWeatherState = _currentWeatherState.asStateFlow()

    private val _forecastState: MutableStateFlow<Result<out ForecastEntity>> = MutableStateFlow(Result.Loading)
    val forecastState = _forecastState.asStateFlow()

    private val _messageSharedFlow = MutableSharedFlow<Int>()
    val messageSharedFlow = _messageSharedFlow.asSharedFlow()

    private val _isRefreshing = MutableStateFlow<Boolean>(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    private val _windSpeedUnitState = MutableStateFlow<WindSpeedUnit>(WindSpeedUnit.METER_PER_SECOND)
    val windSpeedUnitState = _windSpeedUnitState.asStateFlow()

    private lateinit var location: MyLocation

    init {
        viewModelScope.launch {
            settingsRepo.getCurrentLocation().collect {currentLocation ->
                Log.i(TAG, "myLocation: $currentLocation")
                location = currentLocation
                getCurrentWindSpeedUnit()
                loadCurrentWeather(lat = location.lat, long = location.long)
                loadForecast(lat = location.lat, long =  location.long)
            }
        }
    }

    private fun loadForecast(lat: Double, long: Double) {
        viewModelScope.launch {
            try {
                val data = repository.getForecast(lat = lat, long = long).toForecastEntity()


                val tempUnit = settingsRepo.getCurrentTempUnit().first()
                val newList = data.list.map { currentItem ->
                    val newTemp = tempUnit.converter(currentItem.mainData.temp)
                    val tempItem = currentItem.copy(
                        mainData = currentItem.mainData.copy(temp =  newTemp)
                    )
                    tempItem
                }

                _forecastState.update { Result.Success(data.copy(list =  newList)) }
                _messageSharedFlow.emit(R.string.loaded_forecast_successfully)
            } catch (ex: IOException) {
                _forecastState.update { Result.Error(ex.message ?: "Error while getting forecast") }
                _messageSharedFlow.emit(R.string.error_while_getting_forecast)

                loadForecastFromFavorite(lat = lat, long = long)

            }
        }
    }

    private fun loadForecastFromFavorite(lat: Double, long: Double) {
        Log.i(TAG, "loadForecastFromFavorite: started")
        viewModelScope.launch {
            try {
                val data = repository.getFavoriteForecastByLatLong(lat, long)

                val tempUnit = settingsRepo.getCurrentTempUnit().first()
                val newList = data.list.map { currentItem ->
                    val newTemp = tempUnit.converter(currentItem.mainData.temp)
                    val tempItem = currentItem.copy(
                        mainData = currentItem.mainData.copy(temp =  newTemp)
                    )
                    tempItem
                }

                _forecastState.update { Result.Success(data.copy(list = newList)) }
                Log.i(TAG, "loadForecastFromFavorite: success")
            } catch (ex: Exception) {
                Log.e(TAG, "loadForecastFromFavorite: failed")
                Log.e(TAG, "loadForecastFromFavorite: ex: ", ex)
            }
        }

    }

    private fun loadCurrentWeather(lat: Double, long: Double) {
        Log.i(TAG, "loadCurrentWeather: started")
        viewModelScope.launch {
            try {
                val response = repository.getCurrentWeather(lat = lat, long = long)
                val tempUnit = settingsRepo.getCurrentTempUnit().first()

                val newMain = response.main.copy(
                    feelsLike = tempUnit.converter(response.main.feelsLike),
                    temp = tempUnit.converter(response.main.temp)
                )

                _currentWeatherState.update { Result.Success(response.copy(main = newMain)) }
                Log.i(TAG, "loadCurrentWeather: success")
            } catch (ex: Exception) {
                _currentWeatherState.update { Result.Error(ex.message ?: "Error while getting current weather") }
                Log.e(TAG, "loadCurrentWeather: ", ex)
                Log.e(TAG, "loadCurrentWeather: failed")
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            Log.i(TAG, "refresh: started")
            _isRefreshing.update { true }
            delay(500)
            try {
                loadForecast(lat = location.lat, long = location.long)
                loadCurrentWeather(lat = location.lat, long = location.long)
                Log.i(TAG, "refresh: success")
            } catch (ex: Exception) {
                _messageSharedFlow.emit(R.string.error_while_getting_forecast)
                Log.e(TAG, "refresh: failed")
            }
            Log.i(TAG, "refresh: finished")
            _isRefreshing.update { false }
        }
    }

    fun getCurrentWindSpeedUnit() {
        viewModelScope.launch {
            _windSpeedUnitState.update { settingsRepo.getCurrentWindSpeedUnit().first() }
        }
    }



}