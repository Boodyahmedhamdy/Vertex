package com.iti.vertex.home.states

import android.icu.util.TimeZone
import com.iti.vertex.data.dtos.City
import com.iti.vertex.data.dtos.Clouds
import com.iti.vertex.data.dtos.MainData
import com.iti.vertex.data.dtos.SimpleForecastItem

data class ForecastUiState(
    val city: City = City(),
    val list: List<SimpleForecastItem> = listOf(SimpleForecastItem())
)
