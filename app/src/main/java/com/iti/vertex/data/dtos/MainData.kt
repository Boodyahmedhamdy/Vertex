package com.iti.vertex.data.dtos


import com.google.gson.annotations.SerializedName

data class MainData(
    @SerializedName("feels_like")
    val feelsLike: Double = 0.0,
    @SerializedName("grnd_level")
    val groundLevel: Int = 0,
    @SerializedName("humidity")
    val humidity: Int = 0,
    @SerializedName("pressure")
    val pressure: Int = 0,
    @SerializedName("sea_level")
    val seaLevel: Int = 0,
    @SerializedName("temp")
    val temp: Double = 0.0,
    @SerializedName("temp_max")
    val tempMax: Double = 0.0,
    @SerializedName("temp_min")
    val tempMin: Double = 0.0
)