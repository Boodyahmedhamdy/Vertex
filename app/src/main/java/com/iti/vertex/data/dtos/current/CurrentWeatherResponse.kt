package com.iti.vertex.data.dtos.current


import com.google.gson.annotations.SerializedName
import com.iti.vertex.data.dtos.Clouds
import com.iti.vertex.data.dtos.Coord
import com.iti.vertex.data.dtos.MainData
import com.iti.vertex.data.dtos.Weather

data class CurrentWeatherResponse(
    @SerializedName("base")
    val base: String = "",
    @SerializedName("clouds")
    val clouds: Clouds = Clouds(),
    @SerializedName("cod")
    val cod: Int = 0,
    @SerializedName("coord")
    val coord: Coord = Coord(),
    @SerializedName("dt")
    val dt: Int = 0,
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("main")
    val main: MainData = MainData(),
    @SerializedName("name")
    val name: String = "",
    @SerializedName("sys")
    val detailedSys: DetailedSys = DetailedSys(),
    @SerializedName("timezone")
    val timezone: Int = 0,
    @SerializedName("visibility")
    val visibility: Int = 0,
    @SerializedName("weather")
    val weather: List<Weather> = listOf(),
    @SerializedName("wind")
    val simpleWind: SimpleWind = SimpleWind()
)