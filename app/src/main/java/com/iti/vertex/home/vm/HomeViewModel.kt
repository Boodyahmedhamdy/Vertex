package com.iti.vertex.home.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.vertex.R
import com.iti.vertex.data.dtos.current.CurrentWeatherResponse
import com.iti.vertex.data.repos.forecast.IForecastRepository
import com.iti.vertex.home.states.ForecastUiState
import com.iti.vertex.home.states.HomeScreenUiState
import com.iti.vertex.utils.Result
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val TAG = "HomeViewModel"

class HomeViewModel(
    private val repository: IForecastRepository
): ViewModel(){

    private val _state: MutableStateFlow<HomeScreenUiState> = MutableStateFlow(HomeScreenUiState())
    val state = _state.asStateFlow()

    private val _currentWeatherState: MutableStateFlow<Result<out CurrentWeatherResponse>> = MutableStateFlow(Result.Loading)
    val currentWeatherState = _currentWeatherState.asStateFlow()

    private val _forecastState: MutableStateFlow<Result<out ForecastUiState>> = MutableStateFlow(Result.Loading)
    val forecastState = _forecastState.asStateFlow()

    private val _messageSharedFlow = MutableSharedFlow<Int>()
    val messageSharedFlow = _messageSharedFlow.asSharedFlow()

    private val _isRefreshing = MutableStateFlow<Boolean>(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    private fun loadForecast() {
        viewModelScope.launch {
            try {
                val data = repository.getForecast(
                    lat = _state.value.lat,
                    long = _state.value.long
                ).toUiState()
                _forecastState.update { Result.Success(data) }
                _messageSharedFlow.emit(R.string.loaded_forecast_successfully)
            } catch (ex: Exception) {
                _forecastState.update { Result.Error(ex.message ?: "Error while getting forecast") }
                _messageSharedFlow.emit(R.string.error_while_getting_forecast)
            }
        }
    }

    private fun loadCurrentWeather() {
        viewModelScope.launch {
            try {
                val data = repository.getCurrentWeather(lat = _state.value.lat, long = _state.value.long)
                _currentWeatherState.update { Result.Success(data) }
            } catch (ex: Exception) {
                _currentWeatherState.update { Result.Error(ex.message ?: "Error while getting current weather") }
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.update { true }
            delay(1000)
            try {
                loadForecast()
                loadCurrentWeather()
            } catch (ex: Exception) {
                _messageSharedFlow.emit(R.string.error_while_getting_forecast)
            }
            _isRefreshing.update { false }
        }
    }

    fun setLatLong(lat: Double, long: Double) {
        _state.update {
            it.copy(
                lat = lat, long = long
            )
        }
    }

    fun load() {
        loadCurrentWeather()
        loadForecast()
    }


}