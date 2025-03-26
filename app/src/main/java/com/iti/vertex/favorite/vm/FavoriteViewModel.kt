package com.iti.vertex.favorite.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.vertex.data.repos.forecast.ForecastRepository
import com.iti.vertex.data.sources.local.db.entities.ForecastEntity
import com.iti.vertex.favorite.states.FavoriteScreenUiState
import com.iti.vertex.utils.Result
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val TAG = "FavoriteViewModel"

class FavoriteViewModel(
    private val forecastRepository: ForecastRepository
): ViewModel() {

    private val _messageSharedFlow: MutableSharedFlow<String> = MutableSharedFlow()
    val messageSharedFlow = _messageSharedFlow.asSharedFlow()

    private val _favoriteScreenState: MutableStateFlow<Result<out List<ForecastEntity>>> = MutableStateFlow(Result.Loading)
    val favoriteScreenState = _favoriteScreenState.asStateFlow()

    private val _state: MutableStateFlow<FavoriteScreenUiState> = MutableStateFlow(FavoriteScreenUiState(
        notificationMessage = messageSharedFlow
    ))
    val state = _state.asStateFlow()

    init { loadFavoriteItems() }

    private fun loadFavoriteItems() {
        viewModelScope.launch {
            forecastRepository.getFavoriteForecasts()
                .catch { throwable ->
                    _favoriteScreenState.update {
                        Result.Error(throwable.message ?: "ERROR while fetching data from db")
                    }
                    _messageSharedFlow.emit(throwable.message ?: "ERROR")
                }
                .collect { favoriteList ->
                Log.i(TAG, "loadFavoriteItems: collected list of size ${favoriteList.size}")
                _favoriteScreenState.update { Result.Success(favoriteList) }
            }
        }
    }

    fun insertLocationToFavorite(lat: Double, long: Double) {
        viewModelScope.launch {
            try {
                val remoteForecast = forecastRepository.getForecast(lat = lat, long = long)
                val entity = remoteForecast.toForecastEntity()
                forecastRepository.addToFavorite(entity)
                _messageSharedFlow.emit("Added Successfully")
            } catch (ex: Exception) {
                // emitting error while cause the screen to become empty
//                _favoriteScreenState.update { Result.Error(ex.message ?: "ERROR in inserting") }
                _messageSharedFlow.emit("Error ${ex.message}")
            }
        }
    }

    fun getLocationByLatLong(lat: Double, long: Double) {
        viewModelScope.launch {
            Log.i(TAG, "getLocationByLatLong: passed $lat, $long")
            try {
                val entity = forecastRepository.getFavoriteForecastByLatLong(lat = lat, long = long)
                Log.i(TAG, "getLocationByLatLong: $entity")
            } catch (ex: Exception) {
                _messageSharedFlow.emit(ex.message ?: "ERROR WHILE GETTING LOCATION FROM DB")
            }
        }
    }

    fun deleteForecast(entity: ForecastEntity) {
        viewModelScope.launch {
            try {
                forecastRepository.deleteForecast(entity)
                _messageSharedFlow.emit("Deleted ${entity.city.coord} Successfully")
            } catch (ex: Exception) {
                _messageSharedFlow.emit(ex.message ?: "Error while deleting ${entity.city}")
            }
        }
    }

    fun deleteForecastWithResult(entity: ForecastEntity) {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = forecastRepository.deleteForecastWithResult(entity)
            when(result) {
                is Result.Error -> {
                    _state.update { it.copy(isLoading = false) }
                    _messageSharedFlow.emit(result.message)
                }
                Result.Loading -> TODO()
                is Result.Success -> {
                    _state.update { it.copy(isLoading = false) }
                }
            }
        }
    }
}