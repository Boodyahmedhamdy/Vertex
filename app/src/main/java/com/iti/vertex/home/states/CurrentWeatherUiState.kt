package com.iti.vertex.home.states

import com.iti.vertex.R
import com.iti.vertex.data.dtos.Clouds
import com.iti.vertex.data.dtos.MainData
import com.iti.vertex.data.dtos.Weather
import com.iti.vertex.data.dtos.current.DetailedSys
import com.iti.vertex.data.dtos.current.SimpleWind

data class CurrentWeatherUiState(
    val visibility: Int = 0,
    val date: Int = 0,
    val name: String = "",
    val weather: Weather = Weather(),
    val mainData: MainData = MainData(),
    val wind: SimpleWind = SimpleWind(),
    val clouds: Clouds = Clouds(),
    val sys: DetailedSys = DetailedSys()
) {
    fun toConditionsList(): List<SimpleCardConditionItemUiState> = listOf(
        SimpleCardConditionItemUiState(
            imgResId = R.drawable.wind, value = "${wind.speed}",
            unit = R.string.app_name, label = R.string.wind_speed
        ),
        SimpleCardConditionItemUiState(
            imgResId = R.drawable.clouds, value = "${clouds.all}",
            unit = R.string.app_name, label = R.string.clouds
        ),
        SimpleCardConditionItemUiState(
            imgResId = R.drawable.weather_air_pressure, value = "${mainData.pressure}",
            unit = R.string.app_name, label = R.string.pressure
        ),
        SimpleCardConditionItemUiState(
            imgResId = R.drawable.visibility, value = "${visibility}",
            unit = R.string.app_name, label = R.string.visibility
        )
    )
}
