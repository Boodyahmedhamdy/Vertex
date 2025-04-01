package com.iti.vertex.locationpicker.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.iti.vertex.data.repos.forecast.ForecastRepository
import com.iti.vertex.data.repos.settings.SettingsRepository
import com.iti.vertex.utils.Result
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LocationPickerViewModel(
    private val forecastRepo: ForecastRepository,
    private val settingsRepo: SettingsRepository
): ViewModel() {

    private val _uiState: MutableStateFlow<Result<out Unit>> = MutableStateFlow(Result.Loading)
    val uiState = _uiState.asStateFlow()

    private val _locationState: MutableStateFlow<LatLng> = MutableStateFlow(LatLng(0.0, 0.0))
    val locationState = _locationState.asStateFlow()

    private val _searchQueryState = MutableStateFlow("")
    val searchQueryState = _searchQueryState.asStateFlow()

    private val _searchQuerySharedFlow = MutableSharedFlow<String>()
    val searchQuerySharedFlow = _searchQuerySharedFlow.asSharedFlow()

    init {
        loadCurrentLocation()
    }

    fun addSelectedLocationToFavorite() {
        viewModelScope.launch {
            _uiState.update { Result.Loading }
            try {
                val entity = forecastRepo.getForecast(lat = _locationState.value.latitude, long = _locationState.value.longitude).toForecastEntity()
                forecastRepo.addToFavorite(entity)
                _uiState.update { Result.Success(Unit) }
            } catch (ex: Exception) {
                _uiState.update { Result.Error(ex.message ?: "ERROR") }
            }
        }
    }

    fun setAsCurrentLocation() {
        viewModelScope.launch {
            settingsRepo.setCurrentLocation(lat = _locationState.value.latitude, long = _locationState.value.longitude)
        }
    }

    fun updateSearchQuerySharedFlow(query: String) { viewModelScope.launch { _searchQuerySharedFlow.emit(query) } }

    fun updateSearchQueryState(query: String) = _searchQueryState.update { query }

    fun updateLocationState(latLng: LatLng) { _locationState.update { latLng } }

    private fun loadCurrentLocation() {
        viewModelScope.launch {
            settingsRepo.getCurrentLocation()
                .catch {throwable ->
                    _uiState.update { Result.Error(throwable.message ?: "error") }
                }
                .collect {newLocation ->
                    _locationState.update { LatLng(newLocation.lat, newLocation.long) }
                    _uiState.update { Result.Success(Unit) }
                }

        }
    }




}