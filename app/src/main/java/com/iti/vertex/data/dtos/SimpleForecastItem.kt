package com.iti.vertex.data.dtos


import com.google.gson.annotations.SerializedName

data class SimpleForecastItem(
    @SerializedName("clouds")
    val clouds: Clouds = Clouds(),
    @SerializedName("dt")
    val dt: Int = 0,
    @SerializedName("dt_txt")
    val dtTxt: String = "",
    @SerializedName("main")
    val mainData: MainData = MainData(),
    @SerializedName("pop")
    val pop: Int = 0,
    @SerializedName("sys")
    val sys: Sys = Sys(),
    @SerializedName("visibility")
    val visibility: Int = 0,
    @SerializedName("weather")
    val weather: List<Weather> = listOf(),
    @SerializedName("wind")
    val wind: Wind = Wind()
)