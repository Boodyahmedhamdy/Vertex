package com.iti.vertex.settings.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.iti.vertex.data.repos.settings.ISettingsRepository
import com.iti.vertex.data.sources.local.settings.Language
import com.iti.vertex.data.sources.local.settings.LocationProvider
import com.iti.vertex.data.sources.local.settings.TempUnit
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

    private val _languageState: MutableStateFlow<Language> = MutableStateFlow(Language.DEVICE_DEFAULT)
    val languageState = _languageState.asStateFlow()

    private val _tempUnitState: MutableStateFlow<TempUnit> = MutableStateFlow(TempUnit.KELVIN)
    val tempUnitState = _tempUnitState.asStateFlow()

    private val _locationProviderState: MutableStateFlow<LocationProvider> = MutableStateFlow(LocationProvider.GPS)
    val locationProviderState = _locationProviderState.asStateFlow()

    init {
        Log.i(TAG, "init: viewmodel created")
        observeSettings()
    }

    private fun observeSettings() {
        getLanguageState()
        getWindUnitState()
        getTempUnitState()
        getLocationProviderState()
    }

    private fun getTempUnitState() {
        viewModelScope.launch {
            repository.getCurrentTempUnit().collect { newTemp ->
                _tempUnitState.update { newTemp }
            }
        }
    }

    private fun getWindUnitState() {
        viewModelScope.launch {
            repository.getCurrentWindSpeedUnit().collect { newWind ->
                _windSpeedUnitState.update { newWind }
            }
        }
    }

    private fun getLanguageState() {
        viewModelScope.launch {
            repository.getCurrentLanguage().collect { newLang ->
                _languageState.update { newLang }
            }
        }
    }

    private fun getLocationProviderState() {
        viewModelScope.launch {
            repository.getCurrentLocationProvider().collect {newProvider ->
                _locationProviderState.update { newProvider }
            }
        }
    }

    fun setLanguageState(language: Language) {
        viewModelScope.launch {
            repository.setCurrentLanguage(language)
        }
    }

    fun setTempUnit(tempUnit: TempUnit) {
        viewModelScope.launch {
            repository.setCurrentTempUnit(tempUnit)
        }
    }

    fun setWindSpeedUnit(windSpeedUnit: WindSpeedUnit) {
        viewModelScope.launch {
            repository.setWindSpeedUnit(windSpeedUnit)
        }
    }

    fun setLocationProvider(locationProvider: LocationProvider) {
        viewModelScope.launch {
            repository.setCurrentLocationProvider(locationProvider)
        }
    }

}