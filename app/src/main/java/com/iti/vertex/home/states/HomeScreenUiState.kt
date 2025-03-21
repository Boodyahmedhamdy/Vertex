package com.iti.vertex.home.states

import com.iti.vertex.data.dtos.FullForecastResponse

data class HomeScreenUiState(
    val title: String = "HOME-SCREEN-UI-STATE",
    val forecast: FullForecastResponse = FullForecastResponse(),
    val isRefreshing: Boolean = false
) {
}
