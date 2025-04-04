package com.iti.vertex.data.dtos


import com.google.gson.annotations.SerializedName
import com.iti.vertex.data.sources.local.db.entities.ForecastEntity
import com.iti.vertex.home.states.ForecastUiState

data class FullForecastResponse(
    @SerializedName("city")
    val city: City = City(),

    @SerializedName("list")
    val list: List<SimpleForecastItem> = listOf(),

) {
/*    fun toUiState(): ForecastUiState {
        return ForecastUiState(
            city = city,
            list = list
        )
    }*/

    fun toForecastEntity(): ForecastEntity = ForecastEntity(
        city = city,
        list = list
    )
}