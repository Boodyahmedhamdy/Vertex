package com.iti.vertex.home.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.vertex.data.repos.forecast.IForecastRepository
import com.iti.vertex.home.states.HomeScreenUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "HomeViewModel"

class HomeViewModel(
    private val repository: IForecastRepository
): ViewModel(){

    private val _state: MutableStateFlow<HomeScreenUiState> = MutableStateFlow(HomeScreenUiState())
    val state = _state.asStateFlow()

    init {
        loadForecast()
    }


    fun loadForecast() {
        Log.i(TAG, "loadForecast: called")
        viewModelScope.launch {
            val data = repository.getForecast(lat = 30.0444, long = 31.2357)
            withContext(Dispatchers.IO) {
                _state.update {
                    it.copy(forecast = data)
                }
            }
            Log.i(TAG, "loadForecast: data: ${data}")
        }
    }


}