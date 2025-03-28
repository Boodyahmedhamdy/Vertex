package com.iti.vertex.settings.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.iti.vertex.data.repos.settings.ISettingsRepository
import com.iti.vertex.data.repos.settings.SettingsRepository
import com.iti.vertex.data.sources.local.settings.WindSpeedUnit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val TAG = "SettingsViewModel"

class SettingsViewModel(
    private val repository: ISettingsRepository
): ViewModel() {

    private val _windSpeedUnitState: MutableStateFlow<WindSpeedUnit> = MutableStateFlow(WindSpeedUnit.MILE_PER_HOUR)
    val windSpeedUnitState = _windSpeedUnitState.asStateFlow()

    init {
        Log.i(TAG, "init: viewmodel created")
        loadWindSpeedUnit()
    }


    fun setWindSpeedUnit(windSpeedUnit: WindSpeedUnit) {
        Log.i(TAG, "setWindSpeedUnit: setting ${windSpeedUnit.name}")
        viewModelScope.launch {
            repository.setWindSpeedUnit(windSpeedUnit)
            _windSpeedUnitState.update { windSpeedUnit }
        }
    }

    fun loadWindSpeedUnit() {
        Log.i(TAG, "loadWindSpeedUnit: started")
        viewModelScope.launch {
            repository.getCurrentWindSpeedUnit().collect { currentUnit ->
                Log.i(TAG, "loadWindSpeedUnit: converter: ${currentUnit.converter}")
                Log.i(TAG, "loadWindSpeedUnit: current unit ${currentUnit.name}")
            }
        }

    }

}