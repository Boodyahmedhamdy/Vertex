package com.iti.vertex.data.dtos


import com.google.gson.annotations.SerializedName

data class FullForecastResponse(
    @SerializedName("city")
    val city: City = City(),
    @SerializedName("cnt")
    val cnt: Int = 0,
    @SerializedName("cod")
    val cod: String = "",
    @SerializedName("list")
    val list: List<SimpleForecastItem> = listOf(),
    @SerializedName("message")
    val message: Int = 0
)