package com.iti.vertex.favorite.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.vertex.data.repos.forecast.ForecastRepository
import com.iti.vertex.data.repos.forecast.IForecastRepository
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
    private val forecastRepository: IForecastRepository
): ViewModel() {

    private val _messageSharedFlow: MutableSharedFlow<String> = MutableSharedFlow()
    val messageSharedFlow = _messageSharedFlow.asSharedFlow()

    private val _favoriteScreenState: MutableStateFlow<Result<out List<ForecastEntity>>> = MutableStateFlow(Result.Loading)
    val favoriteScreenState = _favoriteScreenState.asStateFlow()

    private val _showDeleteLocationDialog: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val showDeleteLocationDialog = _showDeleteLocationDialog.asStateFlow()

    private val _selectedItemToBeDeleted = MutableStateFlow<ForecastEntity>(ForecastEntity())
    val selectedItemToBeDeleted = _selectedItemToBeDeleted.asStateFlow()

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


    fun toggleShowDeleteLocationDialog() = _showDeleteLocationDialog.update { it.not() }



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

    fun updateSelectedItemToBeDeleted(selectedItem: ForecastEntity) {
        _selectedItemToBeDeleted.update { selectedItem }
    }

}