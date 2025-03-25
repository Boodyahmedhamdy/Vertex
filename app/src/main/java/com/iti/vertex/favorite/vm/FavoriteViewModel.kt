package com.iti.vertex.favorite.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.iti.vertex.data.dtos.City
import com.iti.vertex.data.dtos.Coord
import com.iti.vertex.data.repos.forecast.ForecastRepository
import com.iti.vertex.data.sources.local.db.entities.ForecastEntity
import com.iti.vertex.favorite.FavoriteScreenUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val TAG = "FavoriteViewModel"

class FavoriteViewModel(
    private val forecastRepository: ForecastRepository
): ViewModel() {

    private val _state: MutableStateFlow<FavoriteScreenUiState> = MutableStateFlow(
        FavoriteScreenUiState()
    )
    val state = _state.asStateFlow()

    init {
        Log.i(TAG, "init FavoriteViewModel: ")
        loadFavoriteItems()
    }

    private fun loadFavoriteItems() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            forecastRepository.getFavoriteForecasts().collect { favoriteList ->
                Log.i(TAG, "loadFavoriteItems: collected list of size ${favoriteList.size}")
                _state.update { it.copy(isLoading = false, items = favoriteList) }
            }
        }
    }

    fun insertLocationToFavorite(lat: Double, long: Double) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val remoteForecast = forecastRepository.getForecast(lat = lat, long = long)
            val entity = remoteForecast.toForecastEntity()
            forecastRepository.addToFavorite(entity)
            Log.i(TAG, "insertLocationToFavorite: inserted $entity")
            _state.update { it.copy(isLoading = false) }
        }
    }



}