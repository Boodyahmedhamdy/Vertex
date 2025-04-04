package com.iti.vertex.details.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.vertex.data.repos.forecast.IForecastRepository
import com.iti.vertex.data.repos.settings.ISettingsRepository
import com.iti.vertex.data.sources.local.db.entities.ForecastEntity
import com.iti.vertex.data.sources.local.settings.LocationProvider
import com.iti.vertex.data.sources.local.settings.MyLocation
import com.iti.vertex.utils.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ForecastDetailsViewModel(
    private val repository: IForecastRepository,
    private val settingsRepo: ISettingsRepository
): ViewModel() {

    private val _state: MutableStateFlow<Result<out ForecastEntity>> = MutableStateFlow(Result.Loading)
    val state = _state.asStateFlow()

    fun load(lat: Double, long: Double) {
        viewModelScope.launch {
            try {
                val entity = repository.getFavoriteForecastByLatLong(lat, long)

                val tempUnit = settingsRepo.getCurrentTempUnit().first()
                val newList = entity.list.map { currentItem ->
                    val newTemp = tempUnit.converter(currentItem.mainData.temp)
                    val tempItem = currentItem.copy(
                        mainData = currentItem.mainData.copy(temp =  newTemp)
                    )
                    tempItem
                }

                val finalEntity = entity.copy(list = newList)
                _state.update { Result.Success(finalEntity) }
            } catch (ex: Exception) {
                _state.update { Result.Error(ex.message ?: "Error loading the Forecast") }
            }
        }
    }

    fun setCurrentLocation(myLocation: MyLocation) {
        viewModelScope.launch {
            settingsRepo.setCurrentLocation(myLocation)
            settingsRepo.setCurrentLocationProvider(LocationProvider.MAP)
        }
    }

}