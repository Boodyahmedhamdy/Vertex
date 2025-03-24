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
    @SerializedName("sys")
    val simpleSys: SimpleSys = SimpleSys(),
    @SerializedName("visibility")
    val visibility: Int = 0,
    @SerializedName("weather")
    val weather: List<Weather> = listOf(Weather()),
    @SerializedName("wind")
    val detailedWind: DetailedWind = DetailedWind()
)