package com.iti.vertex.home.states

import android.icu.util.TimeZone
import com.iti.vertex.data.dtos.Clouds
import com.iti.vertex.data.dtos.MainData

data class ForecastUiState(
    val country: String = "EG",
    val name: String = "Al Atabah",
    val sunrise: Long = 324324234,
    val sunset: Long = 6656432,
    val timeZone: Int = 4332,
    val clouds: Clouds = Clouds(all = 34),
    val date: Long = 212342342,
    val dateText: String = "Today",
    val mainData: MainData = MainData(),
    val visibility: Int = 2212
)
