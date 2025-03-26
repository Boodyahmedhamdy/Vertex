package com.iti.vertex.details.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.vertex.data.repos.forecast.IForecastRepository
import com.iti.vertex.data.sources.local.db.entities.ForecastEntity
import com.iti.vertex.utils.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ForecastDetailsViewModel(
    private val repository: IForecastRepository
): ViewModel() {

    private val _state: MutableStateFlow<Result<out ForecastEntity>> = MutableStateFlow(Result.Loading)
    val state = _state.asStateFlow()

    fun load(lat: Double, long: Double) {
        viewModelScope.launch {
            try {
                val entity = repository.getFavoriteForecastByLatLong(lat, long)
                _state.update { Result.Success(entity) }
            } catch (ex: Exception) {
                _state.update { Result.Error(ex.message ?: "Error loading the Forecast") }
            }

        }
    }


}