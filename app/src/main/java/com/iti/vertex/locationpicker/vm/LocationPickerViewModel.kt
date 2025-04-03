package com.iti.vertex.locationpicker.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.api.net.kotlin.awaitFetchPlace
import com.google.android.libraries.places.api.net.kotlin.awaitFindAutocompletePredictions
import com.google.android.libraries.places.compose.autocomplete.models.AutocompletePlace
import com.iti.vertex.data.repos.forecast.ForecastRepository
import com.iti.vertex.data.repos.settings.SettingsRepository
import com.iti.vertex.data.sources.local.settings.MyLocation
import com.iti.vertex.utils.Result
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds


private const val TAG = "LocationPickerViewModel"

class LocationPickerViewModel(
    private val forecastRepo: ForecastRepository,
    private val settingsRepo: SettingsRepository,
    private val placesClient: PlacesClient
): ViewModel() {

    private val _uiState: MutableStateFlow<Result<out Unit>> = MutableStateFlow(Result.Loading)
    val uiState = _uiState.asStateFlow()

    private val _locationState: MutableStateFlow<LatLng> = MutableStateFlow(LatLng(0.0, 0.0))
    val locationState = _locationState.asStateFlow()

    private val _predictionsState = MutableStateFlow(emptyList<AutocompletePrediction>())
    val predictionsState = _predictionsState.asStateFlow()

    private val _searchQuerySharedFlow = MutableSharedFlow<String>()

    init {
        loadCurrentLocation()
        viewModelScope.launch {
            _searchQuerySharedFlow.debounce(1.seconds).distinctUntilChanged().collect {
                if(it.isNotBlank()) fetchLocationPredictions(it)
            }
        }
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
            val myLocation = MyLocation(
                lat = _locationState.value.latitude,
                long = _locationState.value.longitude,
                cityName = ""
            )
            settingsRepo.setCurrentLocation(location = myLocation)
        }
    }

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

    private fun fetchLocationPredictions(query: String) {
        viewModelScope.launch {
            val response = placesClient.awaitFindAutocompletePredictions {
                this.query = query
            }
            _predictionsState.update { response.autocompletePredictions }
        }

    }

    fun fetchPlace(place: AutocompletePlace) {
        Log.i(TAG, "fetchPlace: $place")
        viewModelScope.launch {
            val response = placesClient.awaitFetchPlace(
                placeId = place.placeId,
                placeFields = listOf(Place.Field.LOCATION, Place.Field.DISPLAY_NAME)
            )
            val selectedPlace = response.place

            _locationState.update { selectedPlace.location ?: it }
            _predictionsState.update { emptyList() }
//            _searchQueryState.update { "" }

        }


    }

    fun updateSearchQuerySharedFlow(query: String) {
        viewModelScope.launch { _searchQuerySharedFlow.emit(query) }
    }

}